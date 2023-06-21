package net.starly.core.jb.version.nms.tank;

import lombok.Getter;
import net.starly.core.jb.version.nms.VersionController;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NmsNbtTagCompoundUtil {

    @Getter
    private Class<?> NBTTagCompound;
    @Getter
    private Method getStringMethod;
    @Getter
    private Method setStringMethod;

    NmsNbtTagCompoundUtil(String nbtTagCompoundClassName) throws ClassNotFoundException, NoSuchMethodException {
        try {
            NBTTagCompound = Class.forName(nbtTagCompoundClassName);
        } catch (Exception ignored) {
            NBTTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound");
        }

        VersionController.Version version = VersionController.getInstance().getVersion();
        try {
            getStringMethod = NBTTagCompound.getDeclaredMethod("getString", String.class);
        } catch (Exception e) {
            Map<String, String> methodNameMap = new HashMap<>();
            methodNameMap.put("v1_16_R1", "l");
            methodNameMap.put("v1_16_R2", "l");
            methodNameMap.put("v1_16_R3", "l");
            methodNameMap.put("v1_17_R1", "l");
            methodNameMap.put("v1_18_R1", "l");
            methodNameMap.put("v1_18_R2", "l");
            methodNameMap.put("v1_19_R1", "l");
            methodNameMap.put("v1_19_R2", "l");
            methodNameMap.put("v1_19_R3", "l");
            methodNameMap.put("v1_20_R1", "l");

            getStringMethod = NBTTagCompound.getDeclaredMethod(methodNameMap.get(version.name()), String.class);
        }
        try {
            setStringMethod = NBTTagCompound.getDeclaredMethod("setString", String.class, String.class);
        } catch (Exception e) {
            Map<String, String> methodNameMap = new HashMap<>();
            methodNameMap.put("v1_16_R1", "a");
            methodNameMap.put("v1_16_R2", "a");
            methodNameMap.put("v1_16_R3", "a");
            methodNameMap.put("v1_17_R1", "a");
            methodNameMap.put("v1_18_R1", "a");
            methodNameMap.put("v1_18_R2", "a");
            methodNameMap.put("v1_19_R1", "a");
            methodNameMap.put("v1_19_R2", "a");
            methodNameMap.put("v1_19_R3", "a");
            methodNameMap.put("v1_20_R1", "a");

            setStringMethod = NBTTagCompound.getDeclaredMethod(methodNameMap.get(version.name()), String.class, String.class);
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
