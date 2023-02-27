package net.starly.core.jb.version.nms.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.starly.core.jb.version.nms.tank.NmsItemStackTank;
import net.starly.core.jb.version.nms.tank.NmsItemTank;

@Data
@AllArgsConstructor
public class ItemStackWrapper {

    private Object nmsItemStack;
    private NmsItemTank itemSupport;
    private NmsItemStackTank wrapper;

    public NBTTagCompoundWrapper getTag() {
        try {
            Object obj = wrapper.getGetTagMethod().invoke(nmsItemStack);
            if (obj == null) return null;
            return new NBTTagCompoundWrapper(obj, wrapper.getNbtTagCompoundWrapper());
        } catch (Exception e) {
            return null;
        }
    }

    public void setTag(NBTTagCompoundWrapper tag) {
        try {
            wrapper.getSetTagMethod().invoke(nmsItemStack, tag.getNbtTagCompound());
        } catch (Exception ignored) {}
    }

    public ItemWrapper getItem() {
        return new ItemWrapper(itemSupport, this);
    }

}
