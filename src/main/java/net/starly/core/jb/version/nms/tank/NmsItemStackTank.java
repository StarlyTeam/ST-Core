package net.starly.core.jb.version.nms.tank;

import lombok.Getter;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.VersionController;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public class NmsItemStackTank {

    private static NmsItemStackTank tank;

    private Method bukkitCopyMethod;
    private Method nmsCopyMethod;
    @Getter private Method setTagMethod;
    @Getter private Method getTagMethod;
    @Getter private NmsNbtTagCompoundTank nbtCompoundTank;
    private NmsItemTank nmsItemSupport;

    /**
     * NMS 의 ItemStack 을 얻기 위한 Tank 객체를 가져옵니다.
     * @return NmsItemStackTank
     */
    @Nullable
    public static NmsItemStackTank getInstance() {
        try {
            if(tank == null) tank = new NmsItemStackTank(VersionController.getInstance().getVersion());
            return tank;
        } catch (Exception e) { return null; }
    }

    private NmsItemStackTank(VersionController.Version version) throws ClassNotFoundException, NoSuchMethodException {
        String craftItemStackClassName = "org.bukkit.craftbukkit." + version.getVersion() + ".inventory.CraftItemStack";
        String nmsItemStackClassName = "net.minecraft.server." + version.getVersion() + ".ItemStack";
        nbtCompoundTank = new NmsNbtTagCompoundTank("net.minecraft.server."+ version.getVersion() +".NBTTagCompound");

        Class<?> craftItemStack = Class.forName(craftItemStackClassName);
        Class<?> NMSItemStack;
        try { NMSItemStack = Class.forName(nmsItemStackClassName); }
        catch (Exception e) { NMSItemStack = Class.forName("net.minecraft.world.item.ItemStack"); }
        try { nmsItemSupport = new NmsItemTank("net.minecraft.server."+version.getVersion()+".Item", NMSItemStack); }
        catch (Exception e) { nmsItemSupport = new NmsItemTank("net.minecraft.world.item.Item", NMSItemStack); }
        bukkitCopyMethod = craftItemStack.getDeclaredMethod("asBukkitCopy", NMSItemStack);
        nmsCopyMethod = craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
        try {
            setTagMethod = NMSItemStack.getDeclaredMethod("setTag", nbtCompoundTank.getNBTTagCompound());
        } catch (Exception e) { setTagMethod = NMSItemStack.getDeclaredMethod("a", nbtCompoundTank.getNBTTagCompound()); }
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
