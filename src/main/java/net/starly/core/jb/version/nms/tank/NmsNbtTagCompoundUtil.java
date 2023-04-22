package net.starly.core.jb.version.nms.tank;

import lombok.Getter;
import net.starly.core.jb.version.VersionController;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import net.starly.core.util.collection.STSet;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.bukkit.Bukkit.getServer;

public class NmsNbtTagCompoundUtil {

    @Getter private Class<?> NBTTagCompound;
    @Getter private Method getStringMethod;
    @Getter private Method setStringMethod;

    NmsNbtTagCompoundUtil() throws NoSuchMethodException {
        Optional<String> version = VersionController.getInstance().getVersion();

        if (version.isPresent()) {
            try {
                NBTTagCompound = Class.forName("net.minecraft.server." + version.get() + ".NBTTagCompound");
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                NBTTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound");
            } catch (ClassNotFoundException ignored) {
                STSet<String> versions = VersionController.HIGH_VERSIONS;
                for (String s : versions) {
                    try {
                        NBTTagCompound = Class.forName("net.minecraft.server." + s + ".NBTTagCompound");
                        break;
                    } catch (ClassNotFoundException ignored_) {}
                }
            }
        }

        try {
            getStringMethod = NBTTagCompound.getDeclaredMethod("getString", String.class);
        } catch (NoSuchMethodException ignored) {
            getStringMethod = NBTTagCompound.getDeclaredMethod("l", String.class);
        }
        try {
            setStringMethod = NBTTagCompound.getDeclaredMethod("setString", String.class, String.class);
        } catch (NoSuchMethodException ignored) {
            setStringMethod = NBTTagCompound.getDeclaredMethod("a", String.class, String.class);
        }
    }

    /**
     * NMS 의 NBTTagCompound 를 생성해줍니다.
     *
     * @return NBTTagCompoundWrapper
     */
    public NBTTagCompoundWrapper newInstance() {
        try {
            return new NBTTagCompoundWrapper(NBTTagCompound.newInstance(), this);
        } catch (Exception e) {
            return null;
        }
    }
}
