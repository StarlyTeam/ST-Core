package net.starly.core.jb.version.nms.wrapper

import net.minecraft.server.v1_16_R3.EntityArmorStand
import net.starly.core.jb.util.FeatherLocation
import net.starly.core.jb.version.nms.VersionController
import net.starly.core.jb.version.nms.VersionController.Version
import net.starly.core.jb.version.nms.tank.NmsItemStackUtil
import net.starly.core.jb.version.nms.tank.NmsOtherUtil
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Field
import java.lang.reflect.Method

class ArmorStandWrapper(
    val id: Int,
    var location: FeatherLocation,
    private val entityArmorStand: Any,
) {

    private var defaultHeadPose = NmsOtherUtil.getHeadPose?.invoke(entityArmorStand)

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
        return HeadPoseWrapper(NmsOtherUtil.getHeadPose!!.invoke(entityArmorStand))
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
        if (savePose) defaultHeadPose = NmsOtherUtil.getHeadPose?.invoke(entityArmorStand)
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
        val enumItemSlot = NmsOtherUtil.HeadEnumItemSlot
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

    private fun setShowArms(target: Player) {
        val EntityArmorStandClass: Class<*> = NmsOtherUtil.EntityArmorStandClass
        val setArmsMethod: Method = try {
            EntityArmorStandClass.getMethod("setShowArms", Boolean::class.java)
        } catch (_: Exception) {
            EntityArmorStandClass.getMethod("a", Boolean::class.java)
        }
        setArmsMethod.invoke(entityArmorStand, true)
    }

    private fun entityArmorStandAMethod(b0: Byte, i: Int, flag: Boolean): Byte {
        var b0 = b0
        b0 = if (flag) {
            (b0.toInt() or i).toByte()
        } else {
            (b0.toInt() and i.inv()).toByte()
        }
        return b0
    }

    fun applyMeta(target: Player) {
        setHelmetItem(target)
        setItemInMainHandItem(target)
        setChestplateItem(target)
        setLeggingsItem(target)
        setBootsItem(target)
        if (itemInMainHand != null || itemInOffHand != null) setShowArms(target)
        sendMetaDataPacket(target)
    }

    class HeadPoseWrapper(val x: Float, val y: Float, val z: Float) {

        companion object {
            private val getXMethod = try {
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
            private val getYMethod = try {
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
            private val getZMethod = try {
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
            getXMethod.invoke(obj) as Float,
            getYMethod.invoke(obj) as Float,
            getZMethod.invoke(obj) as Float
        )

        internal val obj get() = NmsOtherUtil.Vector3f.newInstance(x, y, z)

    }

    var itemInMainHand: ItemStack? = null
    var itemInOffHand: ItemStack? = null
    var chestplate: ItemStack? = null
    var leggings: ItemStack? = null
    var boots: ItemStack? = null

    private fun setItemInMainHandItem(target: Player) {
        if (itemInMainHand == null) return
        val args = createArgsArray(NmsOtherUtil.MainHandEnumItemSlot, itemInMainHand)
        NmsOtherUtil.sendPacket(target, NmsOtherUtil.Packet.PacketPlayOutEntityEquipment, *args)
    }

    private fun setChestplateItem(target: Player) {
        if (chestplate == null) return
        val args = createArgsArray(NmsOtherUtil.ChestEnumItemSlot, chestplate)
        NmsOtherUtil.sendPacket(target, NmsOtherUtil.Packet.PacketPlayOutEntityEquipment, *args)
    }

    private fun setLeggingsItem(target: Player) {
        if (leggings == null) return
        val args = createArgsArray(NmsOtherUtil.LegsEnumItemSlot, leggings)
        NmsOtherUtil.sendPacket(target, NmsOtherUtil.Packet.PacketPlayOutEntityEquipment, *args)
    }

    private fun setBootsItem(target: Player) {
        if (boots == null) return
        val args = createArgsArray(NmsOtherUtil.FeetEnumItemSlot, boots)
        NmsOtherUtil.sendPacket(target, NmsOtherUtil.Packet.PacketPlayOutEntityEquipment, *args)
    }

    private fun sendMetaDataPacket(target: Player) {
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


    private fun createArgsArray(enumItemSlot: Any, item: ItemStack?): Array<Any> {
        return if (NmsOtherUtil.highVersion)
            arrayOf(
                id,
                listOf(
                    NmsOtherUtil
                        .Pair!!
                        .newInstance(
                            enumItemSlot,
                            NmsItemStackUtil.getInstance()!!
                                .asNMSCopy(item)!!
                                .nmsItemStack!!
                        )
                )
            )
        else arrayOf(id, enumItemSlot, NmsItemStackUtil.getInstance()!!.asNMSCopy(item)!!.nmsItemStack!!)
    }
}