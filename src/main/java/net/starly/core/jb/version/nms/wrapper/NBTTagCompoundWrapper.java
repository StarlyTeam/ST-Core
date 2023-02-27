package net.starly.core.jb.version.nms.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.starly.core.jb.util.JsonUtil;
import net.starly.core.jb.version.nms.tank.NmsNbtTagCompoundTank;

import java.lang.reflect.InvocationTargetException;

@Data
@AllArgsConstructor
public class NBTTagCompoundWrapper {

    private Object nbtTagCompound;
    private NmsNbtTagCompoundTank wrapper;

    public String getString(String key) {
        try {
            Object result = wrapper.getGetStringMethod().invoke(nbtTagCompound, key);
            if (result == null) return null;
            else return (String) result;
        } catch (Exception e) { return null; }
    }

    public void setString(String key, String value) {
        try {
            wrapper.getSetStringMethod().invoke(nbtTagCompound, key, value);
        } catch (Exception ignored) {}
    }

    public <T> T getObject(Class<T> clazz) {
        try {
            String result = getString(clazz.getSimpleName());
            if (result == null) return null;
            else return JsonUtil.fromJson(result, clazz);
        } catch (Exception e) { return null; }
    }

    public <T> void setObject(T object, Class<T> clazz) {
        try {
            setString(clazz.getSimpleName(), JsonUtil.toJson(object));
        } catch (Exception ignored) {}
    }

}
