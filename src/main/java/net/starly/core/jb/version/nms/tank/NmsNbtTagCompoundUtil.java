package net.starly.core.jb.version.nms.tank;

import lombok.Getter;
import net.starly.core.jb.version.VersionController;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.bukkit.Bukkit.getServer;

public class NmsNbtTagCompoundUtil {

    @Getter private Class<?> NBTTagCompound;
    @Getter private Method getStringMethod;
    @Getter private Method setStringMethod;

    NmsNbtTagCompoundUtil() throws ClassNotFoundException, NoSuchMethodException {
        Optional<String> version = VersionController.getInstance().getVersion();

        if (version.isPresent()) {
            try {
                NBTTagCompound = Class.forName("net.minecraft.server." + version.get() + ".NBTTagCompound");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                NBTTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            getStringMethod = NBTTagCompound.getDeclaredMethod("getString", String.class);
        } catch (Exception e) {
            getStringMethod = NBTTagCompound.getDeclaredMethod("l", String.class);
        }
        try {
            setStringMethod = NBTTagCompound.getDeclaredMethod("setString", String.class, String.class);
        } catch (Exception e) {
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
