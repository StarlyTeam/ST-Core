package net.starly.core.builder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIBuilder {
    private Inventory inv;
    private String title;
    private int size;
    private Player owner;

    public GUIBuilder(String title, int size, Player owner) {
        this.title = title;
        this.size = size;
        this.owner = owner;
        this.inv = Bukkit.createInventory(owner, size, title);
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

    public void setInventory(Inventory inv) {
        this.inv = inv;
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



    public void open() {
        owner.openInventory(inv);
    }

    public void open(Player player) {
        player.openInventory(inv);
    }
}
