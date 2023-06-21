package net.starly.core.jb.version.nms.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.tank.NmsItemUtil;

@AllArgsConstructor
public class ItemStackWrapper {

    /* 코틀린 컴파일러 ㅉㅉ;; */
    @Getter @Setter public Object nmsItemStack;
    @Getter @Setter public NmsItemUtil itemSupport;
    @Getter @Setter public NmsItemStackUtil wrapper;

    /**
     * ItemStack 에 있는 NMSTagCompound 를 가져옵니다.
     *
     * @return NBTTagCompoundWrapper
     */
    public NBTTagCompoundWrapper getTag() {
        try {
            Object obj = wrapper.getGetTagMethod().invoke(nmsItemStack);
            if (obj == null) return null;
            return new NBTTagCompoundWrapper(obj, wrapper.getNbtCompoundUtil());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * ItemStack 에 NBTTagCompound 를 설정합니다.
     *
     * @param tag NBTTagCompoundWrapper
     */
    public void setTag(NBTTagCompoundWrapper tag) {
        try {
            wrapper.getSetTagMethod().invoke(nmsItemStack, tag.getNbtTagCompound());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ItemWrapper getItem() {
        return new ItemWrapper(itemSupport, this);
    }

}
