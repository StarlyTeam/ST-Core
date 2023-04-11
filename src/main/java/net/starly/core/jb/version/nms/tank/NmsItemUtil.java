package net.starly.core.jb.version.nms.tank;

import lombok.Getter;

import java.lang.reflect.Method;

public class NmsItemUtil {

    @Getter private final Class<?> NmsItemClass;
    @Getter private final Class<?> NmsItemStackClass;
    @Getter private Method jMethod;
    @Getter private Method aMethod;

    NmsItemUtil(String className, Class<?> nmsItemStackClass) throws ClassNotFoundException, NoSuchMethodException {
        NmsItemClass = Class.forName(className);
        NmsItemStackClass = nmsItemStackClass;
        jMethod = NmsItemClass.getMethod("j", nmsItemStackClass);
        aMethod = NmsItemClass.getMethod("a", NmsOtherUtil.getInstance().getWorldClass(), NmsOtherUtil.getInstance().getEntityHumanClass(), NmsOtherUtil.getInstance().getEnumHandClass());
    }

}
