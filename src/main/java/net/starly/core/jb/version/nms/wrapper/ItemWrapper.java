package net.starly.core.jb.version.nms.wrapper;

import lombok.Getter;
import net.starly.core.jb.version.nms.VersionController;
import net.starly.core.jb.version.nms.tank.NmsItemUtil;
import net.starly.core.jb.version.nms.tank.NmsOtherUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ItemWrapper {

    private NmsItemUtil support;
    @Getter private Object Item;

    public ItemWrapper(NmsItemUtil itemSupport, ItemStackWrapper nmsItemStackWrapper) {
        try {
            support = itemSupport;
            Method getItemMethod;

            VersionController.Version version = VersionController.getInstance().getVersion();
            try {
                getItemMethod = itemSupport.getNmsItemClass().getMethod("getItem");
            } catch (Exception e) {
                Map<String, String> methodNameMap = new HashMap<>();
                methodNameMap.put("v1_12_R1", "c");
                methodNameMap.put("v1_13_R1", "b");
                methodNameMap.put("v1_13_R2", "b");
                methodNameMap.put("v1_14_R1", "b");
                methodNameMap.put("v1_15_R1", "b");
                methodNameMap.put("v1_16_R2", "b");
                methodNameMap.put("v1_16_R3", "b");
                methodNameMap.put("v1_17_R1", "c");
                methodNameMap.put("v1_18_R1", "c");
                methodNameMap.put("v1_18_R2", "c");
                methodNameMap.put("v1_19_R1", "c");
                methodNameMap.put("v1_19_R2", "c");
                methodNameMap.put("v1_19_R3", "c");
                methodNameMap.put("v1_20_R1", "d");
                methodNameMap.put("v1_20_R2", "d");

                getItemMethod = itemSupport.getNmsItemStackClass().getMethod(methodNameMap.get(version));
            }
            Item = getItemMethod.invoke(nmsItemStackWrapper.getNmsItemStack());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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