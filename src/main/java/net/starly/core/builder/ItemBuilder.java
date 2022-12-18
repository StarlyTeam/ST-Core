package net.starly.core.builder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private final ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        itemMeta.setLore(Arrays.stream(lore).map(s -> ChatColor.translateAlternateColorCodes('&', s)).toList());
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        itemMeta.setLore(Arrays.stream(lore.toArray()).map(s -> ChatColor.translateAlternateColorCodes('&', s.toString())).toList());
        return this;
    }

    public ItemBuilder setCustomModelData(int customModelData) {
        itemMeta.setCustomModelData(customModelData);
        return this;
    }

    public ItemBuilder setItemMeta(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment) {
        itemMeta.addEnchant(enchantment, 1, true);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, Integer level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
