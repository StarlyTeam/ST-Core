package net.starly.core.jb.version.nms.tank;

import lombok.Getter;
import net.starly.core.jb.version.nms.VersionController;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NmsItemStackUtil {

    private static NmsItemStackUtil instance;

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
            if (instance == null) instance = new NmsItemStackUtil(VersionController.getInstance().getVersion());
            return instance;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private NmsItemStackUtil(VersionController.Version version) throws ClassNotFoundException, NoSuchMethodException {
        String craftItemStackClassName = "org.bukkit.craftbukkit." + version.version + ".inventory.CraftItemStack";
        String nmsItemStackClassName = "net.minecraft.server." + version.version + ".ItemStack";
        nbtCompoundUtil = new NmsNbtTagCompoundUtil("net.minecraft.server." + version.version + ".NBTTagCompound");

        Class<?> craftItemStack = Class.forName(craftItemStackClassName);
        Class<?> NMSItemStack;
        try {
            NMSItemStack = Class.forName(nmsItemStackClassName);
        } catch (Exception ignored) {
            NMSItemStack = Class.forName("net.minecraft.world.item.ItemStack");
        }
        try {
            nmsItemSupport = new NmsItemUtil("net.minecraft.server." + version.version + ".Item", NMSItemStack);
        } catch (Exception ignored) {
            nmsItemSupport = new NmsItemUtil("net.minecraft.world.item.Item", NMSItemStack);
        }
        bukkitCopyMethod = craftItemStack.getDeclaredMethod("asBukkitCopy", NMSItemStack);
        nmsCopyMethod = craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
        try {
            setTagMethod = NMSItemStack.getDeclaredMethod("setTag", nbtCompoundUtil.getNBTTagCompoundClass());
        } catch (Exception ignored) {
            Map<String, String> methodNameMap = new HashMap<>();
            methodNameMap.put("v1_16_R1", "c");
            methodNameMap.put("v1_16_R2", "c");
            methodNameMap.put("v1_16_R3", "c");
            methodNameMap.put("v1_17_R1", "c");
            methodNameMap.put("v1_18_R1", "c");
            methodNameMap.put("v1_18_R2", "c");
            methodNameMap.put("v1_19_R1", "c");
            methodNameMap.put("v1_19_R2", "c");
            methodNameMap.put("v1_19_R3", "c");
            methodNameMap.put("v1_20_R1", "c");

            setTagMethod = NMSItemStack.getDeclaredMethod(methodNameMap.get(version.name()), nbtCompoundUtil.getNBTTagCompoundClass());
        }
        try {
            getTagMethod = NMSItemStack.getDeclaredMethod("getTag");
        } catch (Exception ignored) {
            Map<String, String> methodNameMap = new HashMap<>();
            methodNameMap.put("v1_16_R1", "u");
            methodNameMap.put("v1_16_R2", "u");
            methodNameMap.put("v1_16_R3", "u");
            methodNameMap.put("v1_17_R1", "u");
            methodNameMap.put("v1_18_R1", "u");
            methodNameMap.put("v1_18_R2", "u");
            methodNameMap.put("v1_19_R1", "u");
            methodNameMap.put("v1_19_R2", "u");
            methodNameMap.put("v1_19_R3", "u");
            methodNameMap.put("v1_20_R1", "v");

            getTagMethod = NMSItemStack.getDeclaredMethod(methodNameMap.get(version.name()));
        }
    }

    /**
     * ItemStackWrapper 을/를 Bukkit-API 의 ItemStack 으로 변경해줍니다.
     *
     * @param nmsItemStack ItemStackWrapper
     * @return Bukkit-API ItemStack
     */
    public ItemStack asBukkitCopy(ItemStackWrapper nmsItemStack) {
        try {
            return (ItemStack) bukkitCopyMethod.invoke(null, nmsItemStack.getNmsItemStack());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Bukkit-API 의 ItemStack 을/를 ItemStackWrapper 로 변경해줍니다.
     *
     * @param itemStack Bukkit-API ItemStack
     * @return ItemStackWrapper
     */
    @Nullable
    public ItemStackWrapper asNMSCopy(ItemStack itemStack) {
        try {
            return new ItemStackWrapper(nmsCopyMethod.invoke(null, itemStack), nmsItemSupport, this);
        } catch (Exception e) {
            return null;
        }
    }

}
