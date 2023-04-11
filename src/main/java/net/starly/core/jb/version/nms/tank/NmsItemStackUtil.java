package net.starly.core.jb.version.nms.tank;

import lombok.Getter;
import net.starly.core.jb.exception.UnSupportedVersionException;
import net.starly.core.jb.version.VersionController;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.util.collection.STList;
import net.starly.core.util.collection.STSet;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Optional;

public class NmsItemStackUtil {

    private static NmsItemStackUtil tank;

    private Method bukkitCopyMethod;
    private Method nmsCopyMethod;
    @Getter
    private Method setTagMethod;
    @Getter
    private Method getTagMethod;
    @Getter
    private NmsNbtTagCompoundUtil nbtCompoundUtil;
    private NmsItemUtil nmsItemSupport;

    /**
     * NMS 의 ItemStack 을 얻기 위한 Util 객체를 가져옵니다.
     * 이 Util 안에는 NBTTagCompound 를 얻기 위한 Util 객체를 가져갈 수 있는 메서드도 포함되어 있습니다.
     * NmsItemStackUtil#getNbtTagCompoundUtil
     *
     * @return NmsItemStackUtil
     */
    @Nullable
    public static NmsItemStackUtil getInstance() {
        try {
            if (tank == null) tank = new NmsItemStackUtil(VersionController.getInstance().getVersion());
            return tank;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private NmsItemStackUtil(Optional<String> version) throws ClassNotFoundException, NoSuchMethodException, UnSupportedVersionException {
        nbtCompoundUtil = new NmsNbtTagCompoundUtil();


        Class<?> craftItemStack = null;
        if (version.isPresent()) {
            craftItemStack = Class.forName("org.bukkit.craftbukkit." + version.get() + ".inventory.CraftItemStack");
        } else {
            STSet<String> versions = VersionController.HIGH_VERSIONS;
            for (String s : versions) {
                try {
                    craftItemStack = Class.forName("org.bukkit.craftbukkit." + s + ".inventory.CraftItemStack");
                    break;
                } catch (Exception ignored) {}
            }
        }
        if (craftItemStack == null) throw new UnSupportedVersionException(Bukkit.getBukkitVersion() + " 버전은 지원하지 않습니다.");

        Class<?> NMSItemStack = null;
        if (version.isPresent()) {
            try {
                NMSItemStack = Class.forName("net.minecraft.server." + version.get() + ".ItemStack");
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        } else {
            try {
                NMSItemStack = Class.forName("net.minecraft.world.item.ItemStack");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (NMSItemStack == null) throw new UnSupportedVersionException(Bukkit.getBukkitVersion() + " 버전은 지원하지 않습니다.");

        try {
            nmsItemSupport = new NmsItemUtil("net.minecraft.world.item.Item", NMSItemStack);
        } catch (ClassNotFoundException ignored) {
            try {
                nmsItemSupport = new NmsItemUtil("net.minecraft.server." + version.get() + ".Item", NMSItemStack);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }


        bukkitCopyMethod = craftItemStack.getDeclaredMethod("asBukkitCopy", NMSItemStack);
        nmsCopyMethod = craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
        try {
            setTagMethod = NMSItemStack.getDeclaredMethod("setTag", nbtCompoundUtil.getNBTTagCompound());
        } catch (Exception e) { setTagMethod = NMSItemStack.getDeclaredMethod("c", nbtCompoundUtil.getNBTTagCompound()); }
        try { getTagMethod = NMSItemStack.getDeclaredMethod("getTag"); }
        catch (Exception e) { getTagMethod = NMSItemStack.getDeclaredMethod("u"); }
    }

    /**
     * ItemStackWrapper 을/를 Bukkit-API의 ItemStack으로 변경해줍니다.
     *
     * @param nmsItemStack ItemStackWrapper
     * @return Bukkit-API ItemStack
     */
    public ItemStack asBukkitCopy(ItemStackWrapper nmsItemStack) {
        try {
            return (ItemStack) bukkitCopyMethod.invoke(null, nmsItemStack.getNmsItemStack());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Bukkit-API의 ItemStack을 ItemStackWrapper로 변경해줍니다.
     *
     * @param itemStack Bukkit-API ItemStack
     * @return ItemStackWrapper
     */
    @Nullable
    public ItemStackWrapper asNMSCopy(ItemStack itemStack) {
        try {
            return new ItemStackWrapper(nmsCopyMethod.invoke(null, itemStack), nmsItemSupport, this);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
