package net.starly.core.jb.version.nms.tank

import net.starly.core.jb.util.FeatherLocation
import net.starly.core.jb.version.nms.VersionController
import net.starly.core.jb.version.nms.wrapper.ArmorStandWrapper
import net.starly.core.jb.version.nms.wrapper.WorldWrapper
import org.bukkit.Location
import org.bukkit.entity.Player
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

object NmsOtherUtil {

    val version: VersionController.Version = VersionController.getInstance().version
    val highVersion: Boolean = version.highVersion
    private val nmsPackage = "net.minecraft.server." + version.version

    /**
     * Player
     */
    val CraftPlayerClass: Class<*> = Class.forName("org.bukkit.craftbukkit." + version.version + ".entity.CraftPlayer")
    val getHandleAtPlayer: Method = CraftPlayerClass.getMethod("getHandle")
    val EntityPlayerClass: Class<*> =
        try {
            Class.forName("$nmsPackage.EntityPlayer")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.server.level.EntityPlayer")
        }
    val EntityHumanClass: Class<*> =
        try {
            Class.forName("$nmsPackage.EntityHuman")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.world.entity.player.EntityHuman")
        }
    val PlayerConnection: Field =
        try {
            EntityPlayerClass.getField("playerConnection")
        } catch (_: Exception) {
            val fieldNameMap = HashMap<String, String>()
            fieldNameMap["v1_16_R1"] = "b"
            fieldNameMap["v1_16_R2"] = "b"
            fieldNameMap["v1_16_R3"] = "b"
            fieldNameMap["v1_17_R1"] = "b"
            fieldNameMap["v1_18_R1"] = "b"
            fieldNameMap["v1_18_R2"] = "b"
            fieldNameMap["v1_19_R1"] = "b"
            fieldNameMap["v1_19_R2"] = "b"
            fieldNameMap["v1_19_R3"] = "b"
            fieldNameMap["v1_20_R1"] = "c"

            EntityPlayerClass.getField(fieldNameMap.get(version.name) ?: "")
        }
    private val PlayerConnectionClass: Class<*> =
        try {
            Class.forName("$nmsPackage.PlayerConnection")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.server.network.PlayerConnection")
        }
    private val PacketClass: Class<*> =
        try {
            Class.forName("$nmsPackage.Packet")
        } catch (_: Exception) {
            Class.forName("net.minecraft.network.protocol.Packet")
        }
    private val sendPacket: Method =
        try {
            PlayerConnectionClass.getMethod("sendPacket", PacketClass)
        } catch (_: Exception) {
            PlayerConnectionClass.getMethod("a", PacketClass)
        }

    /**
     * World
     */
    val WorldClass: Class<*> =
        try {
            Class.forName("$nmsPackage.World")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.world.level.World")
        }
    private val CraftWorldClass: Class<*> = Class.forName("org.bukkit.craftbukkit." + version.version + ".CraftWorld")
    private val getHandleAtWorld: Method = CraftWorldClass.getMethod("getHandle")

    /**
     * Entity
     */
    private val EntityClass: Class<*> =
        try {
            Class.forName("$nmsPackage.Entity")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.world.entity.Entity")
        }
    private val EntityLivingClass: Class<*> =
        try {
            Class.forName("$nmsPackage.EntityLiving")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.world.entity.EntityLiving")
        }
    private val EntityArmorStandClass: Class<*> =
        try {
            Class.forName("$nmsPackage.EntityArmorStand")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.world.entity.decoration.EntityArmorStand")
        }
    private val DataWatcherClass: Class<*> =
        try {
            Class.forName("$nmsPackage.DataWatcher")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.network.syncher.DataWatcher")
        }

    object Packet {
        val PacketPlayOutSpawnEntity: Constructor<*> =
            try {
                Class.forName("$nmsPackage.PacketPlayOutSpawnEntityLiving").getConstructor(EntityLivingClass)
            } catch (_: Exception) {
                Class.forName("net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity").getConstructor(
                    EntityClass
                )
            }
        val PacketPlayOutEntityDestroy: Constructor<*> =
            try {
                Class.forName("$nmsPackage.PacketPlayOutEntityDestroy").getConstructor(IntArray::class.java)
            } catch (_: Exception) {
                Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy")
                    .getConstructor(IntArray::class.java)
            }
        val PacketPlayOutEntityEquipment: Constructor<*> =
            try {
                Class.forName("$nmsPackage.PacketPlayOutEntityEquipment")
                    .getConstructor(Int::class.java, EnumItemSlot, ItemStackClass)
            } catch (_: Exception) {
                try {
                    Class.forName("$nmsPackage.PacketPlayOutEntityEquipment")
                        .getConstructor(Int::class.java, List::class.java)
                } catch (_: Exception) {
                    Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment")
                        .getConstructor(Int::class.java, List::class.java)
                }
            }
        val PacketPlayOutEntityTeleport: Constructor<*> =
            try {
                Class.forName("$nmsPackage.PacketPlayOutEntityTeleport").getConstructor(EntityClass)
            } catch (_: Exception) {
                Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport").getConstructor(
                    EntityClass
                )
            }
        val PacketPlayOutEntityMetadata: Constructor<*> =
            try {
                Class.forName("$nmsPackage.PacketPlayOutEntityMetadata")
                    .getConstructor(Int::class.java, DataWatcherClass, Boolean::class.java)
            } catch (_: Exception) {
                try {
                    Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata")
                        .getConstructor(Int::class.java, DataWatcherClass, Boolean::class.java)
                } catch (_: Exception) {
                    Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata")
                        .getConstructor(Int::class.java, List::class.java)
                }
            }
    }

    /**
     * MetaData
     */
    val IChatBaseComponentClass: Class<*> =
        try {
            Class.forName("$nmsPackage.IChatBaseComponent")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.network.chat.IChatBaseComponent")
        }
    val ChatSerializerClass: Class<*> =
        try {
            Class.forName("$nmsPackage.IChatBaseComponent\$ChatSerializer")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.network.chat.IChatBaseComponent\$ChatSerializer")
        }
    val EnumItemSlot: Class<*> =
        try {
            Class.forName("$nmsPackage.EnumItemSlot")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.world.entity.EnumItemSlot")
        }
    val ItemStackClass: Class<*> =
        try {
            Class.forName("$nmsPackage.ItemStack")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.world.item.ItemStack")
        }
    val EnumHandClass: Class<*> =
        try {
            Class.forName("$nmsPackage.EnumHand")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.world.EnumHand")
        }
    val EnumMainHand: Any =
        try {
            EnumHandClass.getMethod("valueOf", String::class.java).invoke(null, "MAIN_HAND")
        } catch (_: Exception) {
            val enumNameMap = HashMap<String, String>()
            enumNameMap["v1_16_R1"] = "a"
            enumNameMap["v1_16_R2"] = "a"
            enumNameMap["v1_16_R3"] = "a"
            enumNameMap["v1_17_R1"] = "a"
            enumNameMap["v1_18_R1"] = "a"
            enumNameMap["v1_18_R2"] = "a"
            enumNameMap["v1_19_R1"] = "a"
            enumNameMap["v1_19_R2"] = "a"
            enumNameMap["v1_19_R3"] = "a"
            enumNameMap["v1_20_R1"] = "a"

            EnumHandClass.getMethod("valueOf", String::class.java).invoke(null, enumNameMap[version.name] ?: "")
        }
    val EnumOffHand: Any =
        try {
            EnumHandClass.getMethod("valueOf", String::class.java).invoke(null, "OFF_HAND")
        } catch (_: Exception) {
            val enumNameMap = HashMap<String, String>()
            enumNameMap["v1_16_R1"] = "b"
            enumNameMap["v1_16_R2"] = "b"
            enumNameMap["v1_16_R3"] = "b"
            enumNameMap["v1_17_R1"] = "b"
            enumNameMap["v1_18_R1"] = "b"
            enumNameMap["v1_18_R2"] = "b"
            enumNameMap["v1_19_R1"] = "b"
            enumNameMap["v1_19_R2"] = "b"
            enumNameMap["v1_19_R3"] = "b"
            enumNameMap["v1_20_R1"] = "b"

            EnumHandClass.getMethod("valueOf", String::class.java).invoke(null, enumNameMap[version.name] ?: "")
        }
    val valueOfEnumItemSlot: Method =
        try {
            EnumItemSlot.getMethod("fromName", String::class.java)
        } catch (_: Exception) {
            val methodNameMap = HashMap<String, String>()
            methodNameMap["v1_16_R1"] = "a"
            methodNameMap["v1_16_R2"] = "a"
            methodNameMap["v1_16_R3"] = "a"
            methodNameMap["v1_17_R1"] = "a"
            methodNameMap["v1_18_R1"] = "a"
            methodNameMap["v1_18_R2"] = "a"
            methodNameMap["v1_19_R1"] = "a"
            methodNameMap["v1_19_R2"] = "a"
            methodNameMap["v1_19_R3"] = "a"
            methodNameMap["v1_20_R1"] = "a"

            EnumItemSlot.getMethod(methodNameMap[version.name] ?: "", String::class.java)
        }
    val Vector3fClass: Class<*> =
        try {
            Class.forName("$nmsPackage.Vector3f")
        } catch (_: ClassNotFoundException) {
            Class.forName("net.minecraft.core.Vector3f")
        }
    val Vector3f: Constructor<*> = Vector3fClass.getConstructor(Float::class.java, Float::class.java, Float::class.java)

    @Deprecated("Support 1.13+")
    val Pair: Constructor<*>? = try {
        Class.forName("com.mojang.datafixers.util.Pair").getConstructor(Any::class.java, Any::class.java)
    } catch (_: Exception) {
        null
    }

    @Deprecated("Support 1.13+")
    private val serializeMethod: Method? = try {
        if (highVersion) ChatSerializerClass.getMethod("a", String::class.java)
        else null
    } catch (_: Exception) {
        null
    }

    /**
     * ArmorStand
     */
    private val EntityArmorStand: Constructor<*> =
        EntityArmorStandClass.getConstructor(WorldClass, Double::class.java, Double::class.java, Double::class.java)
    private val getId: Method =
        try {
            EntityArmorStandClass.getMethod("getId")
        } catch (_: Exception) {
            val methodNameMap = HashMap<String, String>()
            methodNameMap["v1_15_R1"] = "S"
            methodNameMap["v1_16_R1"] = "X"
            methodNameMap["v1_16_R2"] = "X"
            methodNameMap["v1_16_R3"] = "Y"
            methodNameMap["v1_17_R1"] = "Z"
            methodNameMap["v1_18_R1"] = "ae"
            methodNameMap["v1_18_R2"] = "ae"
            methodNameMap["v1_19_R1"] = "ae"
            methodNameMap["v1_19_R2"] = "ah"
            methodNameMap["v1_19_R3"] = "af"
            methodNameMap["v1_20_R1"] = "af"

            EntityArmorStandClass.getMethod(methodNameMap[version.name] ?: "")
        }
    val setInvisible: Method =
        try {
            EntityArmorStandClass.getMethod("setInvisible", Boolean::class.java)
        } catch (_: Exception) {
            val methodNameMap = HashMap<String, String>()
            methodNameMap["v1_16_R1"] = "j"
            methodNameMap["v1_16_R2"] = "j"
            methodNameMap["v1_16_R3"] = "j"
            methodNameMap["v1_17_R1"] = "j"
            methodNameMap["v1_18_R1"] = "j"
            methodNameMap["v1_18_R2"] = "j"
            methodNameMap["v1_19_R1"] = "j"
            methodNameMap["v1_19_R2"] = "j"
            methodNameMap["v1_19_R3"] = "j"
            methodNameMap["v1_20_R1"] = "j"

            EntityArmorStandClass.getMethod(methodNameMap[version.name] ?: "", Boolean::class.java)
        }
    val setCustomName: Method =
        try {
            EntityClass.getMethod("setCustomName", String::class.java)
        } catch (_: Exception) {
            val methodNameMap = HashMap<String, String>()
            methodNameMap["v1_16_R1"] = "a"
            methodNameMap["v1_16_R2"] = "a"
            methodNameMap["v1_16_R3"] = "a"
            methodNameMap["v1_17_R1"] = "a"
            methodNameMap["v1_18_R1"] = "a"
            methodNameMap["v1_18_R2"] = "a"
            methodNameMap["v1_19_R1"] = "b"
            methodNameMap["v1_19_R2"] = "b"
            methodNameMap["v1_19_R3"] = "b"
            methodNameMap["v1_20_R1"] = "b"

            EntityClass.getMethod(methodNameMap[version.name] ?: "setCustomName", IChatBaseComponentClass)
        }
    val setCustomNameVisible: Method =
        try {
            EntityArmorStandClass.getMethod("setCustomNameVisible", Boolean::class.java)
        } catch (_: Exception) {
            EntityArmorStandClass.getMethod("n", Boolean::class.java)
        }
    val setSmall: Method =
        try {
            EntityArmorStandClass.getMethod("setSmall", Boolean::class.java)
        } catch (_: Exception) {
            val methodNameMap = HashMap<String, String>()
            methodNameMap["v1_16_R1"] = "a"
            methodNameMap["v1_16_R2"] = "a"
            methodNameMap["v1_16_R3"] = "a"
            methodNameMap["v1_17_R1"] = "a"
            methodNameMap["v1_18_R1"] = "a"
            methodNameMap["v1_18_R2"] = "a"
            methodNameMap["v1_19_R1"] = "a"
            methodNameMap["v1_19_R2"] = "a"
            methodNameMap["v1_19_R3"] = "t"
            methodNameMap["v1_20_R1"] = "t"

            EntityArmorStandClass.getMethod(methodNameMap[version.name] ?: "", Boolean::class.java)
        }
    val setLocation: Method =
        try {
            EntityArmorStandClass.getMethod(
                "setLocation",
                Double::class.java,
                Double::class.java,
                Double::class.java,
                Float::class.java,
                Float::class.java
            )
        } catch (_: Exception) {
            val methodNameMap = HashMap<String, String>()
            methodNameMap["v1_16_R1"] = "a"
            methodNameMap["v1_16_R2"] = "a"
            methodNameMap["v1_16_R3"] = "a"
            methodNameMap["v1_17_R1"] = "a"
            methodNameMap["v1_18_R1"] = "a"
            methodNameMap["v1_18_R2"] = "a"
            methodNameMap["v1_19_R1"] = "a"
            methodNameMap["v1_19_R2"] = "a"
            methodNameMap["v1_19_R3"] = "a"
            methodNameMap["v1_20_R1"] = "a"

            EntityArmorStandClass.getMethod(
                methodNameMap[version.name] ?: "",
                Double::class.java,
                Double::class.java,
                Double::class.java,
                Float::class.java,
                Float::class.java
            )
        }
    val getDataWatcher: Method =
        try {
            EntityArmorStandClass.getMethod("getDataWatcher")
        } catch (_: Exception) {
            val methodNameMap = HashMap<String, String>()
            methodNameMap["v1_16_R1"] = "Y"
            methodNameMap["v1_16_R2"] = "aa"
            methodNameMap["v1_16_R3"] = "ab"
            methodNameMap["v1_17_R1"] = "ad"
            methodNameMap["v1_18_R1"] = "ai"
            methodNameMap["v1_18_R2"] = "ai"
            methodNameMap["v1_19_R1"] = "ai"
            methodNameMap["v1_19_R2"] = "al"
            methodNameMap["v1_19_R3"] = "aj"

            EntityArmorStandClass.getMethod(methodNameMap[version.name] ?: "")
        }
    val setHeadPose: Method =
        try {
            EntityArmorStandClass.getMethod("setHeadPose", Vector3fClass)
        } catch (_: Exception) {
            val methodNameMap = HashMap<String, String>()
            methodNameMap["v1_16_R1"] = "a"
            methodNameMap["v1_16_R2"] = "a"
            methodNameMap["v1_16_R3"] = "a"
            methodNameMap["v1_17_R1"] = "a"
            methodNameMap["v1_18_R1"] = "a"
            methodNameMap["v1_18_R2"] = "a"
            methodNameMap["v1_19_R1"] = "a"
            methodNameMap["v1_19_R2"] = "a"
            methodNameMap["v1_19_R3"] = "a"
            methodNameMap["v1_20_R1"] = "a"

            EntityArmorStandClass.getMethod(methodNameMap[version.name] ?: "", Vector3fClass)
        }
    val getHeadPose: Method =
        try {
            EntityArmorStandClass.getMethod("getHeadPose")
        } catch (_: Exception) {
            val methodNameMap = HashMap<String, String>()
            methodNameMap["v1_16_R1"] = "r"
            methodNameMap["v1_16_R2"] = "r"
            methodNameMap["v1_16_R3"] = "r"
            methodNameMap["v1_17_R1"] = "v"
            methodNameMap["v1_18_R1"] = "u"
            methodNameMap["v1_18_R2"] = "u"
            methodNameMap["v1_19_R1"] = "u"
            methodNameMap["v1_19_R2"] = "u"
            methodNameMap["v1_19_R3"] = "x"
            methodNameMap["v1_20_R1"] = "x"

            EntityArmorStandClass.getMethod(methodNameMap[version.name] ?: "")
        }

    @Deprecated("Support 1.19+")
    val getNonDefaultValues: Method? = try {
        DataWatcherClass.getMethod("c")
    } catch (_: Exception) {
        null
    }

    fun createArmorStandInstance(
        location: Location,
        block: (ArmorStandWrapper) -> Unit = {}
    ): ArmorStandWrapper {
        val loc = toFeatherLocation(location)
        val armorStand = EntityArmorStand.newInstance(loc.world.world, location.x, location.y, location.z)
        return ArmorStandWrapper(
            if (getId.invoke(armorStand) == null) {
                0
            } else {
                getId.invoke(armorStand) as Int
            }, loc, armorStand
        ).apply(block)
    }

    fun toFeatherLocation(location: Location): FeatherLocation {
        val wrapper = WorldWrapper(location.world!!, getHandleAtWorld.invoke(CraftWorldClass.cast(location.world)))
        return FeatherLocation(wrapper, location.x, location.y, location.z, location.yaw, location.pitch)
    }

    internal fun toVersionString(string: String): Any =
        serializeMethod?.invoke(null, "{\"text\": \"§f$string\"}") ?: string

    internal fun sendPacket(target: Player, packetConstructor: Constructor<*>, vararg args: Any) {
        val packet = (if (args.isEmpty()) packetConstructor.newInstance() else packetConstructor.newInstance(*args))
        sendPacket.invoke(PlayerConnection.get(getHandleAtPlayer.invoke(CraftPlayerClass.cast(target))), packet)
    }

}