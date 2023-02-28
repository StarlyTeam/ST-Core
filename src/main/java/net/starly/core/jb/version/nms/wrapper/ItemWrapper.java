package net.starly.core.jb.version.nms.wrapper;

import jdk.jfr.events.ExceptionThrownEvent;
import lombok.Getter;
import net.starly.core.jb.version.nms.tank.NmsItemUtil;
import net.starly.core.jb.version.nms.tank.NmsOtherUtil;
import org.bukkit.entity.Player;

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

    public Object getEnumInteractionResult(WorldWrapper world, Player player, Object enumHand) {
        try {
            return support.getAMethod().invoke(
                    world.getWorld(),
                    NmsOtherUtil.INSTANCE.getGetHandleAtPlayer().invoke(NmsOtherUtil.INSTANCE.getCraftPlayerClass().cast(player)),
                    enumHand
            );
        } catch (Exception ignored) { return null; }
    }

}
