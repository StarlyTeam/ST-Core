package net.starly.core.jb.version.nms.wrapper

import net.starly.core.jb.util.FeatherLocation
import net.starly.core.jb.version.VersionController
import net.starly.core.jb.version.nms.tank.NmsOtherUtil
import net.starly.core.jb.version.nms.tank.NmsItemStackUtil
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ArmorStandWrapper(
    val id: Int,
    var location: FeatherLocation,
    private val entityArmorStand: Any,
) {

    private val defaultHeadPose = NmsOtherUtil.headPose.get(entityArmorStand)

    var displayName = ""
        set(value) {
            field = value
            NmsOtherUtil.setCustomName.invoke(entityArmorStand, NmsOtherUtil.toVersionString(value))
        }

    var small = true
        set(value) {
            field = value
            NmsOtherUtil.setSmall.invoke(entityArmorStand, value)
        }

    var invisible = true
        set(value) {
            field = value
            NmsOtherUtil.setInvisible.invoke(entityArmorStand, value)
        }

    var customNameVisible = false
        set(value) {
            field = value
            NmsOtherUtil.setCustomNameVisible.invoke(entityArmorStand, value)
        }

    var helmet: ItemStack? = null

    fun setHeadPose(x: Float, y: Float, z: Float) {
        NmsOtherUtil.headPose.set(entityArmorStand, NmsOtherUtil.Vector3f.newInstance(x, y, z))
    }

    fun resetHeadPose() {
        NmsOtherUtil.headPose.set(entityArmorStand, defaultHeadPose)
    }

    fun teleport(target: Player, location: Location) {
        val wrapper = NmsOtherUtil.toFeatherLocation(location)
        NmsOtherUtil.setLocation.invoke(entityArmorStand, wrapper.x, wrapper.y, wrapper.z, wrapper.yaw, wrapper.pitch)
        NmsOtherUtil.sendPacket(target, NmsOtherUtil.Packet.PacketPlayOutEntityTeleport, entityArmorStand)
        this.location = wrapper
    }

    fun spawn(target: Player) {
        NmsOtherUtil.sendPacket(target, NmsOtherUtil.Packet.PacketPlayOutSpawnEntity, entityArmorStand)
    }

    fun remove(target: Player) {
        NmsOtherUtil.sendPacket(target, NmsOtherUtil.Packet.PacketPlayOutEntityDestroy, id)
    }

    private fun setHelmetItem(target: Player) {
        if(helmet == null) return
        val enumItemSlot = NmsOtherUtil.valueOfEnumItemSlot.invoke(null, "head")
        val args = if(NmsOtherUtil.highVersion)
            arrayOf(id,
                listOf(NmsOtherUtil
                    .Pair!!
                    .newInstance(enumItemSlot,
                        NmsItemStackUtil.getInstance()!!
                        .asNMSCopy(helmet)!!
                        .nmsItemStack!!
                    )
                )
            )
        else arrayOf(id, enumItemSlot, NmsItemStackUtil.getInstance()!!.asNMSCopy(helmet)!!.nmsItemStack!!)
        NmsOtherUtil.sendPacket(target, NmsOtherUtil.Packet.PacketPlayOutEntityEquipment, *args)
    }

    fun applyMeta(target: Player) {
        setHelmetItem(target)
        if(NmsOtherUtil.version == VersionController.Version.v1_19_R2) {
            NmsOtherUtil.sendPacket(
                target,
                NmsOtherUtil.Packet.PacketPlayOutEntityMetadata,
                id,
                NmsOtherUtil.getNonDefaultValues!!.invoke(NmsOtherUtil.getDataWatcher.invoke(entityArmorStand))
            )
        } else {
            NmsOtherUtil.sendPacket(
                target,
                NmsOtherUtil.Packet.PacketPlayOutEntityMetadata,
                id,
                NmsOtherUtil.getDataWatcher.invoke(entityArmorStand),
                true
            )
        }
    }

}