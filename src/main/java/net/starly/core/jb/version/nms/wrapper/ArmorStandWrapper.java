package net.starly.core.jb.version.nms.wrapper;

import net.starly.core.jb.util.FeatherLocation;
import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.tank.NmsOtherUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ArmorStandWrapper {
    private int id;
    private FeatherLocation location;
    private Object entityArmorStand;
    private EulerAngle defaultHeadPose;

    public ArmorStandWrapper(int id, FeatherLocation location, Object entityArmorStand) {
        this.id = id;
        this.location = location;
        this.entityArmorStand = entityArmorStand;

        try { defaultHeadPose = (EulerAngle) NmsOtherUtil.getInstance().getGetHeadPose().invoke(entityArmorStand); }
        catch (Exception ex) { defaultHeadPose = null; }
    }

    private String displayName = "";
    private boolean small = true;
    private boolean invisible = true;
    private boolean customNameVisible = false;
    private ItemStack helmet = null;


    public void setDisplayName(String value) {
        this.displayName = value;
        try { NmsOtherUtil.getInstance().getSetCustomName().invoke(entityArmorStand, NmsOtherUtil.getInstance().toVersionString(value)); }
        catch (Exception ex) { ex.printStackTrace(); }
    }

    public void setSmall(boolean value) {
        this.small = value;
        try { NmsOtherUtil.getInstance().getSetSmall().invoke(entityArmorStand, value); }
        catch (Exception ex) { ex.printStackTrace(); }
    }

    public void setInvisible(boolean value) {
        this.invisible = value;
        try { NmsOtherUtil.getInstance().getSetInvisible().invoke(entityArmorStand, value); }
        catch (Exception ex) { ex.printStackTrace(); }
    }

    public void setCustomNameVisible(boolean value) {
        this.customNameVisible = value;
        try { NmsOtherUtil.getInstance().getSetCustomNameVisible().invoke(entityArmorStand, value); }
        catch (Exception ex) { ex.printStackTrace(); }
    }

    public HeadPoseWrapper getHeadPose() {
        try { return new HeadPoseWrapper(NmsOtherUtil.getInstance().getGetHeadPose().invoke(entityArmorStand)); }
        catch (Exception ex) { ex.printStackTrace(); return null; }
    }

    public void setHeadPose(HeadPoseWrapper pose) {
        try { NmsOtherUtil.getInstance().getSetHeadPose().invoke(entityArmorStand, pose.getObj()); }
        catch (Exception ex) { ex.printStackTrace(); }
    }

    public void resetHeadPose() {
        try { NmsOtherUtil.getInstance().getSetHeadPose().invoke(entityArmorStand, defaultHeadPose); }
        catch (Exception ex) { ex.printStackTrace(); }
    }

    public void teleport(Player target, Location location, Boolean savePose) {
        if (savePose == null) savePose = false;
        FeatherLocation wrapper = NmsOtherUtil.getInstance().toFeatherLocation(location);
        try { NmsOtherUtil.getInstance().getSetLocation().invoke(entityArmorStand, wrapper.getX(), wrapper.getY(), wrapper.getZ(), wrapper.getYaw(), wrapper.getPitch()); }
        catch (Exception ex) { ex.printStackTrace(); }
        NmsOtherUtil.getInstance().sendPacket(target, NmsOtherUtil.getInstance().getPacketInstance().getPacketPlayOutEntityTeleport(), entityArmorStand);
        this.location = wrapper;
        if (savePose) {
            try {
                defaultHeadPose = (EulerAngle) NmsOtherUtil.getInstance().getGetHeadPose().invoke(entityArmorStand);
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    public void spawn(Player target) {
        NmsOtherUtil.getInstance().sendPacket(target, NmsOtherUtil.getInstance().getPacketInstance().getPacketPlayOutSpawnEntity(), entityArmorStand);
    }

    public void remove(Player target) {
        NmsOtherUtil.getInstance().sendPacket(target, NmsOtherUtil.getInstance().getPacketInstance().getPacketPlayOutEntityDestroy(), new int[]{id});
    }

    private void setHelmetItem(Player target) {
        if (helmet == null) return;
        Object enumItemSlot;
        try { enumItemSlot = NmsOtherUtil.getInstance().getValueOfEnumItemSlot().invoke(null, "head"); }
        catch (Exception ex) { ex.printStackTrace(); return; }
        Object[] args = new Object[]{};
        if (NmsOtherUtil.getInstance().isHighVersion()) {
            try {
                args = new Object[]{
                        id,
                        Arrays.asList(
                                NmsOtherUtil
                                        .getInstance()
                                        .getPair()
                                        .newInstance(enumItemSlot,
                                                NmsItemStackUtil
                                                        .getInstance()
                                                        .asNMSCopy(helmet)
                                                        .getNmsItemStack()
                                        )
                        )
                };
            } catch (Exception ex) { ex.printStackTrace(); }
        } else {
            try {
                args = new Object[]{
                        id,
                        enumItemSlot,
                        NmsItemStackUtil.getInstance().asNMSCopy(helmet).getNmsItemStack()
                };
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        NmsOtherUtil.getInstance().sendPacket(target, NmsOtherUtil.getInstance().getPacketInstance().getPacketPlayOutEntityEquipment(), args);
    }

    public void applyMeta(Player target) {
        setHelmetItem(target);
        if (!NmsOtherUtil.getInstance().getVersion().isPresent()) {
            try {
                NmsOtherUtil.getInstance().sendPacket(
                        target,
                        NmsOtherUtil.getInstance().getPacketInstance().getPacketPlayOutEntityMetadata(),
                        id,
                        NmsOtherUtil.getInstance().getGetNonDefaultValues().invoke(NmsOtherUtil.getInstance().getGetDataWatcher().invoke(entityArmorStand))
                );
            } catch (Exception ex) { ex.printStackTrace(); }
        } else {
            try {
                NmsOtherUtil.getInstance().sendPacket(
                        target,
                        NmsOtherUtil.getInstance().getPacketInstance().getPacketPlayOutEntityMetadata(),
                        id,
                        NmsOtherUtil.getInstance().getGetDataWatcher().invoke(entityArmorStand),
                        true
                );
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}
