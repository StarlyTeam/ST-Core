package net.starly.core.jb.version.nms.wrapper

import net.starly.core.jb.util.FeatherLocation
import net.starly.core.jb.version.nms.VersionController
import net.starly.core.jb.version.nms.VersionController.Version
import net.starly.core.jb.version.nms.tank.NmsItemStackUtil
import net.starly.core.jb.version.nms.tank.NmsOtherUtil
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import kotlin.collections.HashMap

class ArmorStandWrapper(
    val id: Int,
    var location: FeatherLocation,
    private val entityArmorStand: Any,
) {

    private var defaultHeadPose = NmsOtherUtil.getHeadPose.invoke(entityArmorStand)

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

    fun getHeadPose(): HeadPoseWrapper {
        return HeadPoseWrapper(NmsOtherUtil.getHeadPose.invoke(entityArmorStand))
    }

    fun setHeadPose(pose: HeadPoseWrapper) {
        NmsOtherUtil.setHeadPose.invoke(entityArmorStand, pose.obj)
    }

    fun resetHeadPose() {
        NmsOtherUtil.setHeadPose.invoke(entityArmorStand, defaultHeadPose)
    }

    fun teleport(target: Player, location: Location, savePose: Boolean = false) {
        val wrapper = NmsOtherUtil.toFeatherLocation(location)
        NmsOtherUtil.setLocation.invoke(entityArmorStand, wrapper.x, wrapper.y, wrapper.z, wrapper.yaw, wrapper.pitch)
        NmsOtherUtil.sendPacket(target, NmsOtherUtil.Packet.PacketPlayOutEntityTeleport, entityArmorStand)
        this.location = wrapper
        if (savePose) defaultHeadPose = NmsOtherUtil.getHeadPose.invoke(entityArmorStand)
    }

    fun spawn(target: Player) {
        NmsOtherUtil.sendPacket(target, NmsOtherUtil.Packet.PacketPlayOutSpawnEntity, entityArmorStand)
        applyMeta(target)
    }

    fun remove(target: Player) {
        NmsOtherUtil.sendPacket(target, NmsOtherUtil.Packet.PacketPlayOutEntityDestroy, arrayOf(id).toIntArray())
    }

    private fun setHelmetItem(target: Player) {
        if (helmet == null) return
        val enumItemSlot = NmsOtherUtil.valueOfEnumItemSlot.invoke(null, "head")
        val args = if (NmsOtherUtil.highVersion)
            arrayOf(
                id,
                listOf(
                    NmsOtherUtil
                        .Pair!!
                        .newInstance(
                            enumItemSlot,
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
        try {
            NmsOtherUtil.sendPacket(
                target,
                NmsOtherUtil.Packet.PacketPlayOutEntityMetadata,
                id,
                NmsOtherUtil.getDataWatcher.invoke(entityArmorStand),
                true
            )
        } catch (_: Exception) {
            NmsOtherUtil.sendPacket(
                target,
                NmsOtherUtil.Packet.PacketPlayOutEntityMetadata,
                id,
                NmsOtherUtil.getNonDefaultValues!!.invoke(NmsOtherUtil.getDataWatcher.invoke(entityArmorStand))
            )
        }
    }

    class HeadPoseWrapper(val x: Float, val y: Float, val z: Float) {

        companion object {
            private val xMethod = try {
                NmsOtherUtil.Vector3fClass.getMethod("getX")
            } catch (_: Exception) {
                val methodNameMap = HashMap<String, String>()
                methodNameMap["v1_16_R1"] = "b"
                methodNameMap["v1_16_R2"] = "b"
                methodNameMap["v1_16_R3"] = "b"
                methodNameMap["v1_17_R1"] = "b"
                methodNameMap["v1_18_R1"] = "b"
                methodNameMap["v1_18_R2"] = "b"
                methodNameMap["v1_19_R1"] = "b"
                methodNameMap["v1_19_R2"] = "b"
                methodNameMap["v1_19_R3"] = "b"
                methodNameMap["v1_20_R1"] = "b"

                val version: Version = VersionController.getInstance().version
                NmsOtherUtil.Vector3fClass.getMethod(methodNameMap[version.name] ?: "")
            }
            private val yMethod = try {
                NmsOtherUtil.Vector3fClass.getMethod("getY")
            } catch (_: Exception) {
                val methodNameMap = HashMap<String, String>()
                methodNameMap["v1_16_R1"] = "c"
                methodNameMap["v1_16_R2"] = "c"
                methodNameMap["v1_16_R3"] = "c"
                methodNameMap["v1_17_R1"] = "c"
                methodNameMap["v1_18_R1"] = "c"
                methodNameMap["v1_18_R2"] = "c"
                methodNameMap["v1_19_R1"] = "c"
                methodNameMap["v1_19_R2"] = "c"
                methodNameMap["v1_19_R3"] = "c"
                methodNameMap["v1_20_R1"] = "c"

                val version: Version = VersionController.getInstance().version
                NmsOtherUtil.Vector3fClass.getMethod(methodNameMap[version.name] ?: "")
            }
            private val zMethod = try {
                NmsOtherUtil.Vector3fClass.getMethod("getZ")
            } catch (_: Exception) {
                val methodNameMap = HashMap<String, String>();
                methodNameMap["v1_16_R1"] = "d"
                methodNameMap["v1_16_R2"] = "d"
                methodNameMap["v1_16_R3"] = "d"
                methodNameMap["v1_17_R1"] = "d"
                methodNameMap["v1_18_R1"] = "d"
                methodNameMap["v1_18_R2"] = "d"
                methodNameMap["v1_19_R1"] = "d"
                methodNameMap["v1_19_R2"] = "d"
                methodNameMap["v1_19_R3"] = "d"
                methodNameMap["v1_20_R1"] = "d"

                val version: Version = VersionController.getInstance().version
                NmsOtherUtil.Vector3fClass.getMethod(methodNameMap[version.name] ?: "")
            }
        }

        constructor(obj: Any) : this(
            xMethod.invoke(obj) as Float,
            yMethod.invoke(obj) as Float,
            zMethod.invoke(obj) as Float
        )

        internal val obj get() = NmsOtherUtil.Vector3f.newInstance(x, y, z)

    }

}