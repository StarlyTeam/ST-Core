package net.starly.core.jb.version.nms.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.tank.NmsItemUtil;

@Data
@AllArgsConstructor
public class ItemStackWrapper {

    private Object nmsItemStack;
    public Object getNmsItemStack() { return nmsItemStack; }
    private NmsItemUtil itemSupport;
    private NmsItemStackUtil wrapper;

    /**
     * ItemStack 에 있는 NMSTagCompound 를 가져옵니다.
     * @return NBTTagCompoundWrapper
     */
    public NBTTagCompoundWrapper getTag() {
        try {
            Object obj = wrapper.getGetTagMethod().invoke(nmsItemStack);
            if (obj == null) return null;
            return new NBTTagCompoundWrapper(obj, wrapper.getNbtCompoundUtil());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ItemStack 에 NBTTagCompound 를 설정합니다.
     * @param tag NBTTagCompoundWrapper
     */
    public void setTag(NBTTagCompoundWrapper tag) {
        try {
            wrapper.getSetTagMethod().invoke(nmsItemStack, tag.getNbtTagCompound());
        } catch (Exception ignored) {
        }
    }

    public ItemWrapper getItem() {
        return new ItemWrapper(itemSupport, this);
    }

}
