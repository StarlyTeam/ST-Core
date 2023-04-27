package net.starly.core.jb.version.nms.tank;

import lombok.Getter;
import net.starly.core.jb.util.FeatherLocation;
import net.starly.core.jb.version.VersionController;
import net.starly.core.jb.version.nms.wrapper.ArmorStandWrapper;
import net.starly.core.jb.version.nms.wrapper.WorldWrapper;
import net.starly.core.util.collection.STSet;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.bukkit.Bukkit.getServer;

@Getter
public class NmsOtherUtil {

    private static NmsOtherUtil instance;

    public static NmsOtherUtil getInstance() {
        if (instance == null) instance = new NmsOtherUtil();
        return instance;
    }

    private Packet packetInstance;

    public Packet getPacketInstance() {
        if (packetInstance == null) packetInstance = new Packet();
        return packetInstance;
    }

    private final Optional<String> version = VersionController.getInstance().getVersion();
    private final boolean highVersion = !version.isPresent();
    protected final String nmsPackage = "net.minecraft.server." + (version.orElse("UNDEFINED"));

    /**
     * Player
     */
    private Class<?> CraftPlayerClass;
    private Method getHandleAtPlayer;
    private Class<?> EntityPlayerClass;
    private Class<?> EntityHumanClass;
    private Field PlayerConnection;
    private Class<?> PlayerConnectionClass;
    private Class<?> PacketClass;
    private Method sendPacket;

    /**
     * World
     */
    private Class<?> WorldClass;
    private Class<?> CraftWorldClass;
    private Method getHandleAtWorld;

    /**
     * Entity
     */
    private Class<?> EntityClass;
    private Class<?> EntityLivingClass;
    private Class<?> EntityArmorStandClass;
    private Class<?> DataWatcherClass;


    private Class<?> IChatBaseComponentClass;
    private Class<?> ChatSerializerClass;
    private Class<?> EnumItemSlot;
    private Class<?> ItemStackClass;
    private Class<?> EnumHandClass;
    private Object EnumMainHand;
    private Object EnumOffHand;
    private Method valueOfEnumItemSlot;
    private Class<?> Vector3fClass;
    private Constructor<?> Vector3f;
    private Constructor<?> Pair;
    private Method serializeMethod;

    /**
     * ArmorStand
     */
    private Constructor<?> EntityArmorStand;
    private Method getId;
    private Method setInvisible;
    private Method setCustomName;
    private Method setCustomNameVisible;
    private Method setSmall;
    private Method setLocation;
    private Method getDataWatcher;
    private Method setHeadPose;
    private Method getHeadPose;
    @Deprecated
    private Method getNonDefaultValues;

    public ArmorStandWrapper createArmorStandInstance(Location location, Consumer<ArmorStandWrapper> block) {
        try {
            FeatherLocation loc = toFeatherLocation(location);
            ArmorStandWrapper armorStand = (ArmorStandWrapper) EntityArmorStand.newInstance(loc.getWorld().getWorld(), location.getX(), location.getY(), location.getZ());

            ArmorStandWrapper armorStandWrapper = new ArmorStandWrapper((int) getId.invoke(armorStand), loc, armorStand);
            block.accept(armorStandWrapper);
            return armorStandWrapper;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public FeatherLocation toFeatherLocation(Location location) {
        try {
            WorldWrapper wrapper = new WorldWrapper(location.getWorld(), getHandleAtWorld.invoke(CraftWorldClass.cast(location.getWorld())));
            return new FeatherLocation(wrapper, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Object toVersionString(String string) {
        try {
            return serializeMethod.invoke(null, "{\"text\": \"§f" + string + "\"}");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void sendPacket(Player target, Constructor<?> packetConstructor, Object... args) {
        try {
            Object packet = null;
            if (args.length == 0) {
                packet = packetConstructor.newInstance();
            } else {
                packetConstructor.newInstance(args);
            }

            sendPacket.invoke(PlayerConnection.get(getHandleAtPlayer.invoke(CraftPlayerClass.cast(target))), packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private NmsOtherUtil() {

        String v = null;
        if (highVersion) {
            STSet<String> versions = VersionController.HIGH_VERSIONS;
            for (String s : versions) {
                try {
                    Class.forName("org.bukkit.craftbukkit." + s + ".inventory.CraftItemStack");
                    v = s;
                    break;
                } catch (Exception ignored) {
                }
            }
        } else v = version.get();

        try {
            CraftPlayerClass = Class.forName("org.bukkit.craftbukkit." + v + ".entity.CraftPlayer");

            getHandleAtPlayer = CraftPlayerClass.getMethod("getHandle");

            try {
                EntityPlayerClass = Class.forName(nmsPackage + ".EntityPlayer");
            } catch (Exception ignored) {
                EntityPlayerClass = Class.forName("net.minecraft.server.level.EntityPlayer");
            }

            try {
                EntityHumanClass = Class.forName(nmsPackage + ".EntityHuman");
            } catch (Exception ignored) {
                EntityHumanClass = Class.forName("net.minecraft.world.entity.player.EntityHuman");
            }

            try {
                PlayerConnection = EntityPlayerClass.getField("playerConnection");
            } catch (Exception ignored) {
                PlayerConnection = EntityPlayerClass.getField("b");
            }

            try {
                PlayerConnectionClass = Class.forName(nmsPackage + ".PlayerConnection");
            } catch (Exception ignored) {
                PlayerConnectionClass = Class.forName("net.minecraft.server.network.PlayerConnection");
            }

            try {
                PacketClass = Class.forName(nmsPackage + ".Packet");
            } catch (Exception ignored) {
                PacketClass = Class.forName("net.minecraft.network.protocol.Packet");
            }

            try {
                sendPacket = PlayerConnectionClass.getMethod("sendPacket", PacketClass);
            } catch (Exception ignored) {
                sendPacket = PlayerConnectionClass.getMethod("a", PacketClass);
            }

            try {
                WorldClass = Class.forName(nmsPackage + ".World");
            } catch (Exception ignored) {
                WorldClass = Class.forName("net.minecraft.world.level.World");
            }

            CraftWorldClass = Class.forName("org.bukkit.craftbukkit." + v + ".CraftWorld");

            getHandleAtWorld = CraftWorldClass.getMethod("getHandle");

            try {
                EntityClass = Class.forName(nmsPackage + ".Entity");
            } catch (Exception ignored) {
                EntityClass = Class.forName("net.minecraft.world.entity.Entity");
            }

            try {
                EntityLivingClass = Class.forName(nmsPackage + ".EntityLiving");
            } catch (Exception ignored) {
                EntityLivingClass = Class.forName("net.minecraft.world.entity.EntityLiving");
            }

            try {
                EntityArmorStandClass = Class.forName(nmsPackage + ".EntityArmorStand");
            } catch (Exception ignored) {
                EntityArmorStandClass = Class.forName("net.minecraft.world.entity.decoration.EntityArmorStand");
            }

            try {
                DataWatcherClass = Class.forName(nmsPackage + ".DataWatcher");
            } catch (Exception ignored) {
                DataWatcherClass = Class.forName("net.minecraft.network.syncher.DataWatcher");
            }

            try {
                IChatBaseComponentClass = Class.forName(nmsPackage + ".IChatBaseComponent");
            } catch (Exception ignored) {
                IChatBaseComponentClass = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
            }

            try {
                ChatSerializerClass = Class.forName(nmsPackage + ".IChatBaseComponent$ChatSerializer");
            } catch (Exception ignored) {
                ChatSerializerClass = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");
            }

            try {
                EnumItemSlot = Class.forName(nmsPackage + ".EnumItemSlot");
            } catch (Exception ignored) {
                EnumItemSlot = Class.forName("net.minecraft.world.entity.EnumItemSlot");
            }

            try {
                ItemStackClass = Class.forName(nmsPackage + ".ItemStack");
            } catch (Exception ignored) {
                ItemStackClass = Class.forName("net.minecraft.world.item.ItemStack");
            }

            try {
                EnumHandClass = Class.forName(nmsPackage + ".EnumHand");
            } catch (Exception ignored) {
                EnumHandClass = Class.forName("net.minecraft.world.EnumHand");
            }

            try {
                EnumMainHand = EnumHandClass.getMethod("valueOf", String.class).invoke(null, "MAIN_HAND");
            } catch (Exception ignored) {
                EnumMainHand = EnumHandClass.getMethod("valueOf", String.class).invoke(null, "a");
            }

            try {
                EnumOffHand = EnumHandClass.getMethod("valueOf", String.class).invoke(null, "OFF_HAND");
            } catch (Exception ignored) {
                EnumMainHand = EnumHandClass.getMethod("valueOf", String.class).invoke(null, "b");
            }

            try {
                valueOfEnumItemSlot = EnumItemSlot.getMethod("valueOf", String.class);
            } catch (Exception ignored) {
                valueOfEnumItemSlot = EnumItemSlot.getMethod("valueOf", String.class);
            }

            try {
                Vector3fClass = Class.forName(nmsPackage + ".Vector3f");
            } catch (Exception ignored) {
                Vector3fClass = Class.forName("net.minecraft.core.Vector3f");
            }

            Vector3f = Vector3fClass.getConstructor(float.class, float.class, float.class);

            if (highVersion) {
                try {
                    serializeMethod = ChatSerializerClass.getMethod("a", String.class);
                } catch (Exception ignored) {
                }
            } else serializeMethod = null;

            EntityArmorStand = EntityArmorStandClass.getConstructor(WorldClass, double.class, double.class, double.class);

            try {
                getId = EntityArmorStandClass.getMethod("getId");
            } catch (Exception ignored) {
                if (v.contains("v1_19")) getId = EntityArmorStandClass.getMethod("ae");
                else getId = EntityArmorStandClass.getMethod("ah");
            }

            try {
                setInvisible = EntityArmorStandClass.getMethod("setInvisible", boolean.class);
            } catch (Exception ignored) {
                setInvisible = EntityArmorStandClass.getMethod("j", boolean.class);
            }

            try {
                setCustomName = EntityArmorStandClass.getMethod("setCustomName", String.class);
            } catch (Exception ignored) {
                try {
                    setCustomName = EntityArmorStandClass.getMethod("setCustomName", IChatBaseComponentClass);
                } catch (Exception ignored_) {
                    setCustomName = EntityArmorStandClass.getMethod("b", IChatBaseComponentClass);
                }
            }

            try {
                setCustomNameVisible = EntityArmorStandClass.getMethod("setCustomNameVisible", boolean.class);
            } catch (Exception ignored) {
                setCustomNameVisible = EntityArmorStandClass.getMethod("n", boolean.class);
            }

            try {
                setSmall = EntityArmorStandClass.getMethod("setSmall", boolean.class);
            } catch (Exception ignored) {
                setSmall = EntityArmorStandClass.getMethod("a", boolean.class);
            }

            try {
                setLocation = EntityArmorStandClass.getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
            } catch (Exception ignored) {
                setLocation = EntityArmorStandClass.getMethod("a", double.class, double.class, double.class, float.class, float.class);
            }

            try {
                getDataWatcher = EntityArmorStandClass.getMethod("getDataWatcher");
            } catch (Exception ignored) {
                try {
                    getDataWatcher = EntityArmorStandClass.getMethod("al");
                } catch (Exception ignored_) {
                    getDataWatcher = EntityArmorStandClass.getMethod("ai");
                }
            }

            try {
                setHeadPose = EntityArmorStandClass.getMethod("setHeadPose", Vector3fClass);
            } catch (Exception ignored) {
                setHeadPose = EntityArmorStandClass.getMethod("a", Vector3fClass);
            }

            try {
                getHeadPose = EntityArmorStandClass.getMethod("getHeadPose");
            } catch (Exception ignored) {
                try {
                    getHeadPose = EntityArmorStandClass.getMethod("v");
                } catch (Exception ignored_) {
                    try {
                        getHeadPose = EntityArmorStandClass.getMethod("u");
                    } catch (Exception ignored__) {
                        getHeadPose = EntityArmorStandClass.getMethod("x");
                    }
                }
            }

            try {
                getNonDefaultValues = DataWatcherClass.getMethod("c");
            } catch (Exception ignored) {
                getNonDefaultValues = null;
            }
        } catch (Exception ignored) {
            getServer().getLogger().warning("Almost NMS classes is working, but some of them is not working.\n이 메세지는 오류가 아닙니다.\n버전에 따라 다를 수 있으며, 기능이 작동되지 않을 경우 스탈리 스토어로 문의해주시기 바랍니다.");
        }
    }

    @Getter
    public class Packet {

        private Constructor<?> PacketPlayOutSpawnEntity;
        private Constructor<?> PacketPlayOutEntityDestroy;
        private Constructor<?> PacketPlayOutEntityEquipment;
        private Constructor<?> PacketPlayOutEntityTeleport;
        private Constructor<?> PacketPlayOutEntityMetadata;


        Packet() {
            try {
                try {
                    PacketPlayOutSpawnEntity = Class.forName(nmsPackage + ".PacketPlayOutSpawnEntityLiving").getConstructor(EntityLivingClass);
                } catch (Exception ignored) {
                    PacketPlayOutSpawnEntity = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity").getConstructor(EntityClass);
                }

                try {
                    PacketPlayOutEntityDestroy = Class.forName(nmsPackage + ".PacketPlayOutEntityDestroy").getConstructor(int.class, EnumItemSlot, ItemStackClass);
                } catch (Exception ignored) {
                    try {
                        Class.forName(nmsPackage + ".PacketPlayOutEntityEquipment").getConstructor(int.class, List.class);
                    } catch (Exception ignored_) {
                        Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment").getConstructor(int.class, List.class);
                    }
                }

                try {
                    PacketPlayOutEntityEquipment = Class.forName(nmsPackage + ".PacketPlayOutEntityEquipment").getConstructor(int.class, EnumItemSlot, ItemStackClass);
                } catch (Exception ignored) {
                    try {
                        Class.forName(nmsPackage + ".PacketPlayOutEntityEquipment").getConstructor(int.class, List.class);
                    } catch (Exception ignored_) {
                        Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment").getConstructor(int.class, List.class);
                    }
                }

                try {
                    PacketPlayOutEntityTeleport = Class.forName(nmsPackage + ".PacketPlayOutEntityTeleport").getConstructor(EntityClass);
                } catch (Exception ignored) {
                    PacketPlayOutEntityTeleport = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport").getConstructor(EntityClass);
                }

                try {
                    PacketPlayOutEntityMetadata = Class.forName(nmsPackage + ".PacketPlayOutEntityMetadata").getConstructor(int.class, DataWatcherClass, boolean.class);
                } catch (Exception ignored) {
                    try {
                        PacketPlayOutEntityMetadata = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata").getConstructor(int.class, DataWatcherClass, boolean.class);
                    } catch (Exception ignored_) {
                        PacketPlayOutEntityMetadata = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata").getConstructor(int.class, List.class);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
