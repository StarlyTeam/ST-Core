package net.starly.core.jb.version.nms.tank;

import lombok.Getter;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.VersionController;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public class NmsItemStackUtil {

    private static NmsItemStackUtil tank;

    private Method bukkitCopyMethod;
    private Method nmsCopyMethod;
    @Getter private Method setTagMethod;
    @Getter private Method getTagMethod;
    @Getter private NmsNbtTagCompoundUtil nbtCompoundUtil;
    private NmsItemUtil nmsItemSupport;

    /**
     * NMS 의 ItemStack 을 얻기 위한 Util 객체를 가져옵니다.
     * 이 Util 안에는 NBTTagCompound 를 얻기 위한 Util 객체를 가져갈 수 있는 메서드도 포함되어 있습니다.
     * NmsItemStackUtil#getNbtTagCompoundUtil
     * @return NmsItemStackUtil
     */
    @Nullable
    public static NmsItemStackUtil getInstance() {
        try {
            if(tank == null) tank = new NmsItemStackUtil(VersionController.getInstance().getVersion());
            return tank;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private NmsItemStackUtil(VersionController.Version version) throws ClassNotFoundException, NoSuchMethodException {
        String craftItemStackClassName = "org.bukkit.craftbukkit." + version.getVersion() + ".inventory.CraftItemStack";
        String nmsItemStackClassName = "net.minecraft.server." + version.getVersion() + ".ItemStack";
        nbtCompoundUtil = new NmsNbtTagCompoundUtil("net.minecraft.server."+ version.getVersion() +".NBTTagCompound");

        Class<?> craftItemStack = Class.forName(craftItemStackClassName);
        Class<?> NMSItemStack;
        try { NMSItemStack = Class.forName(nmsItemStackClassName); }
        catch (Exception e) { NMSItemStack = Class.forName("net.minecraft.world.item.ItemStack"); }
        try { nmsItemSupport = new NmsItemUtil("net.minecraft.server."+version.getVersion()+".Item", NMSItemStack); }
        catch (Exception e) { nmsItemSupport = new NmsItemUtil("net.minecraft.world.item.Item", NMSItemStack); }
        bukkitCopyMethod = craftItemStack.getDeclaredMethod("asBukkitCopy", NMSItemStack);
        nmsCopyMethod = craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
        try {
            setTagMethod = NMSItemStack.getDeclaredMethod("setTag", nbtCompoundUtil.getNBTTagCompound());
        } catch (Exception e) { setTagMethod = NMSItemStack.getDeclaredMethod("c", nbtCompoundUtil.getNBTTagCompound()); }
        try { getTagMethod = NMSItemStack.getDeclaredMethod("getTag"); }
        catch (Exception e) { getTagMethod = NMSItemStack.getDeclaredMethod("u"); }
    }

    /**
     * ItemStackWrapper 을/를 Bukkit-API 의 ItemStack 으로 변경해줍니다.
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
