package net.starly.core.builder;

import net.starly.core.StarlyCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder setLore(List<String> lore) {
        itemMeta.setLore(Arrays.stream(lore.toArray()).map(s -> ChatColor.translateAlternateColorCodes('&', s.toString())).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        lore.add(ChatColor.translateAlternateColorCodes('&', line));
        itemMeta.setLore(lore);
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

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, Integer level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        itemMeta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder setNBT(String key, String value) {
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(JavaPlugin.getProvidingPlugin(StarlyCore.class), key), PersistentDataType.STRING, value);
        return this;
    }

    public ItemBuilder removeNBT(String key) {
        itemMeta.getPersistentDataContainer().remove(new NamespacedKey(JavaPlugin.getProvidingPlugin(StarlyCore.class), key));
        return this;
    }

    public ItemBuilder setPDC(String key, String value, PersistentDataType type, JavaPlugin plugin) {
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), type, value);
        return this;
    }

    public ItemBuilder removePDC(String key, JavaPlugin plugin) {
        itemMeta.getPersistentDataContainer().remove(new NamespacedKey(plugin, key));
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
