package net.starly.core.jb.container.button;

import lombok.Getter;
import net.starly.core.jb.container.STContainer;
import net.starly.core.jb.container.wrapper.ButtonClickEventWrapper;
import net.starly.core.jb.util.PlayerSkullManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class STButton {

    @Getter private boolean isGlow;
    @Getter private boolean isCleanable;
    private BiConsumer<ButtonClickEventWrapper, STContainer> function;
    private ItemStack itemStack;
    private String displayName;
    private List<String> lore;

    private STButton() {
        isGlow = false;
        isCleanable = true;
        displayName = null;
        lore = null;
    }

    private STButton apply() {
        ItemMeta meta = itemStack.getItemMeta();
        if(displayName != null) meta.setDisplayName(displayName);
        if(lore != null) meta.setLore(lore);
        if(isGlow) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        if(isGlow) itemStack.addUnsafeEnchantment(Enchantment.LURE, 1);
        return this;
    }

    public ItemStack getOriginalItemStack() { return  itemStack; }
    public ItemStack getItemStack() {return itemStack.clone(); }
    public String getDisplayName() { return displayName == null ? "" : displayName; }
    public List<String> getLore() { return lore == null ? Collections.emptyList() : lore; }

    public void setSlot(STContainer container, int... slots) {
        for(int slot: slots)
            container.registerButton(slot, this);
    }

    public void execute(STContainer container, ButtonClickEventWrapper wrapper) {
        if(function == null) return;
        function.accept(wrapper, container);
    }

    public static class STButtonBuilder {

        private STButton button;

        private STButtonBuilder() {
            button = new STButton();
        }

        public STButtonBuilder(ItemStack itemStack) {
            this();
            button.itemStack = itemStack.clone();
            if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) button.lore = itemStack.getItemMeta().getLore();
        }
        public STButtonBuilder(Material material) {
            this();
            button.itemStack = new ItemStack(material);
        }
        public STButtonBuilder(Material material, int amount) {
            this();
            button.itemStack = new ItemStack(material, amount);
        }
        public STButtonBuilder(Player player) {
            this();
            button.itemStack = PlayerSkullManager.getPlayerSkull(player.getUniqueId());
        }
        public STButtonBuilder(UUID uniqueId) {
            this();
            button.itemStack = PlayerSkullManager.getPlayerSkull(uniqueId);
        }

        public STButtonBuilder setDisplayName(String displayName) {
            button.displayName = displayName;
            return this;
        }

        public STButtonBuilder setLore(List<String> lore) {
            button.lore = lore;
            return this;
        }

        public STButtonBuilder setLore(String... lore) {
            button.lore = Arrays.stream(lore).collect(Collectors.toList());
            return this;
        }

        public STButtonBuilder setGlow(boolean glow) {
            button.isGlow = glow;
            return this;
        }

        public STButtonBuilder setCleanable(boolean cleanable) {
            button.isCleanable = cleanable;
            return this;
        }

        public STButtonBuilder setClickFunction(BiConsumer<ButtonClickEventWrapper, STContainer> func) {
            button.function = func;
            return this;
        }

        public STButton build() {
            return button.apply();
        }
    }

}
