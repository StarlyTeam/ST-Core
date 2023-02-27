package net.starly.core.jb.version.nms.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.starly.core.jb.util.JsonUtil;
import net.starly.core.jb.version.nms.tank.NmsNbtTagCompoundUtil;

@Data
@AllArgsConstructor
public class NBTTagCompoundWrapper {

    private Object nbtTagCompound;
    private NmsNbtTagCompoundUtil wrapper;

    /**
     * NBTTagCompound 에서 String 으로 설정된 값을 가져옵니다.
     * @param key 키 값
     * @return value 값
     */
    public String getString(String key) {
        try {
            Object result = wrapper.getGetStringMethod().invoke(nbtTagCompound, key);
            if (result == null) return null;
            else return (String) result;
        } catch (Exception e) { return null; }
    }

    /**
     * NBTTagCompound 에 String 값을 설정합니다.
     * @param key 키 값
     * @param value value 값
     */
    public void setString(String key, String value) {
        try {
            wrapper.getSetStringMethod().invoke(nbtTagCompound, key, value);
        } catch (Exception ignored) {}
    }

    /**
     * NBTTagCompound 에서 저장된 객체를 가져옵니다.
     * @param clazz 객체 클래스
     * @return 객체 ( 없을 시, null )
     * @param <T> 해당 객체의 타입
     */
    public <T> T getObject(Class<T> clazz) {
        try {
            String result = getString(clazz.getSimpleName());
            if (result == null) return null;
            else return JsonUtil.fromJson(result, clazz);
        } catch (Exception e) { return null; }
    }

    /**
     * NBTTagCompound 에 객체를 저장합니다.
     * @param object 객체
     * @param clazz 객체 클래스
     * @param <T> 해당 객체의 타입
     */
    public <T> void setObject(T object, Class<T> clazz) {
        try {
            setString(clazz.getSimpleName(), JsonUtil.toJson(object));
        } catch (Exception ignored) {}
    }

}
