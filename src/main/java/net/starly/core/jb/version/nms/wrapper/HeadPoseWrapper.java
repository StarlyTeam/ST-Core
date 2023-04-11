package net.starly.core.jb.version.nms.wrapper;

import net.starly.core.jb.version.nms.tank.NmsOtherUtil;

import java.lang.reflect.Method;

public class HeadPoseWrapper {
    private float x;
    private float y;
    private float z;

    private Method xMethod;
    private Method yMethod;
    private Method zMethod;

    public HeadPoseWrapper(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;

        init();
    }

    public HeadPoseWrapper(Object obj) {
        try {
            this.x = (float) xMethod.invoke(obj);
            this.y = (float) yMethod.invoke(obj);
            this.z = (float) zMethod.invoke(obj);
        } catch (Exception ex) { ex.printStackTrace(); }

        init();
    }

    public Object getObj() {
        try {
            return NmsOtherUtil.getInstance().getVector3f().newInstance(x, y, z);
        } catch (Exception ex) { ex.printStackTrace(); return null; }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    private void init() {
        try {
            try {
                xMethod = NmsOtherUtil.getInstance().getVector3fClass().getDeclaredMethod("getX");
            } catch (Exception ex) {
                xMethod = NmsOtherUtil.getInstance().getVector3fClass().getDeclaredMethod("b");
            }

            try {
                yMethod = NmsOtherUtil.getInstance().getVector3fClass().getDeclaredMethod("getY");
            } catch (Exception ex) {
                yMethod = NmsOtherUtil.getInstance().getVector3fClass().getDeclaredMethod("c");
            }

            try {
                zMethod = NmsOtherUtil.getInstance().getVector3fClass().getDeclaredMethod("getZ");
            } catch (Exception ex) {
                zMethod = NmsOtherUtil.getInstance().getVector3fClass().getDeclaredMethod("d");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
