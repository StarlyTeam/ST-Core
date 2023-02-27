package net.starly.core.jb.version.nms.wrapper;

import lombok.Getter;
import net.starly.core.jb.version.nms.tank.NmsItemUtil;

import java.lang.reflect.Method;

public class ItemWrapper {

    private NmsItemUtil support;
    @Getter private Object Item;

    public ItemWrapper(NmsItemUtil itemSupport, ItemStackWrapper nmsItemStackWrapper) {
        try {
            support = itemSupport;
            Method getItemMethod;
            try {
                getItemMethod = itemSupport.getNmsItemStackClass().getMethod("getItem");
            } catch (Exception e) { getItemMethod = itemSupport.getNmsItemStackClass().getMethod("c"); }
            Item = getItemMethod.invoke(nmsItemStackWrapper.getNmsItemStack());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 아이템의 현지화되지 않은 이름을 가져옵니다.
     * @param nmsItemStack ItemStackWrapper
     * @return 현지화되지 않은 이름
     */
    public String getUnlocalizedName(ItemStackWrapper nmsItemStack) {
        try {
            return (String) support.getJMethod().invoke(Item, nmsItemStack.getNmsItemStack());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
