package net.starly.core.jb.version.nms.tank;

import lombok.Getter;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.VersionController;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NmsItemStackTank {

    private static NmsItemStackTank tank;

    private Method bukkitCopyMethod;
    private Method nmsCopyMethod;
    @Getter private Method setTagMethod;
    @Getter private Method getTagMethod;
    @Getter private NmsNbtTagCompoundTank nbtTagCompoundWrapper;
    private NmsItemTank nmsItemSupport;

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
        NmsNbtTagCompoundTank nbtTagCompoundWrapper = new NmsNbtTagCompoundTank("net.minecraft.server."+ version.getVersion() +".NBTTagCompound");

        Class<?> craftItemStack = Class.forName(craftItemStackClassName);
        Class<?> NMSItemStack;
        try { NMSItemStack = Class.forName(nmsItemStackClassName); }
        catch (Exception e) { NMSItemStack = Class.forName("net.minecraft.world.item.ItemStack"); }
        try { nmsItemSupport = new NmsItemTank("net.minecraft.server."+version.getVersion()+".Item", NMSItemStack); }
        catch (Exception e) { nmsItemSupport = new NmsItemTank("net.minecraft.world.item.Item", NMSItemStack); }
        bukkitCopyMethod = craftItemStack.getDeclaredMethod("asBukkitCopy", NMSItemStack);
        nmsCopyMethod = craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
        try {
            setTagMethod = NMSItemStack.getDeclaredMethod("setTag", nbtTagCompoundWrapper.getNBTTagCompound());
        } catch (Exception e) { setTagMethod = NMSItemStack.getDeclaredMethod("a", nbtTagCompoundWrapper.getNBTTagCompound()); }
        try { getTagMethod = NMSItemStack.getDeclaredMethod("getTag"); }
        catch (Exception e) { getTagMethod = NMSItemStack.getDeclaredMethod("u"); }
    }

    public ItemStack asBukkitCopy(ItemStackWrapper nmsItemStack) {
        try {
            return (ItemStack) bukkitCopyMethod.invoke(null, nmsItemStack.getNmsItemStack());
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public ItemStackWrapper asNMSCopy(ItemStack itemStack) {
        try {
            return new ItemStackWrapper(nmsCopyMethod.invoke(null, itemStack), nmsItemSupport, this);
        } catch (Exception e) {
            return null;
        }
    }

}
