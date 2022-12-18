package net.starly.core.builder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class GUIBuilder {
    private Inventory inv;
    private String title;
    private int size;
    private Player owner;

    public GUIBuilder(String title, int line, Player owner) {
        if (!(line >= 1 && line <= 6))
            throw new IllegalArgumentException("GUI 사이즈는 1~6 사이의 값이어야 합니다.");

        this.title = title;
        this.size = line * 9;
        this.owner = owner;
        this.inv = Bukkit.createInventory(owner, line, title);
    }

    public Inventory getInventory() {
        return inv;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public Player getOwner() {
        return owner;
    }


    public void setItem(int slot, Material material) {
        setItem(slot, material, 1);
    }

    public void setItem(int slot, Material material, int amount) {
        setItem(slot, new ItemBuilder(material, amount));
    }

    public void setItem(int slot, ItemBuilder item) {
        setItem(slot, item.build());
    }

    public void setItem(Map<Integer, ItemStack> items) {
        items.forEach(this::setItem);
    }

    public void setItem(ItemStack item, List<Integer> slots) {
        for (int slot : slots) {
            setItem(slot, item);
        }
    }

    public void setItem(ItemStack item, int... slots) {
        for (int slot : slots) {
            setItem(slot, item);
        }
    }

    public void setItem(int slot, ItemStack item) {
        inv.setItem(slot, item);
    }

    public void addItem(Material material) {
        addItem(material, 1);
    }

    public void addItem(Material material, int amount) {
        addItem(new ItemBuilder(material, amount));
    }

    public void addItem(ItemBuilder item) {
        addItem(item.build());
    }

    public void addItem(ItemStack item) {
        inv.addItem(item);
    }

    public void open(Player player) {
        player.openInventory(inv);
    }
}
