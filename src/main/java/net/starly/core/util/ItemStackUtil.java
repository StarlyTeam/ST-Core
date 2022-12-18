package net.starly.core.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemStackUtil {
    public static ItemStack getPlayerHead(OfflinePlayer p) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta sm = (SkullMeta) head.getItemMeta(); sm.setOwningPlayer(p); head.setItemMeta(sm);

        return head;
    }

    public static ItemStack getPlayerHead(Player p) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta sm = (SkullMeta) head.getItemMeta(); sm.setOwningPlayer(Bukkit.getOfflinePlayer(p.getUniqueId())); head.setItemMeta(sm);

        return head;
    }

    public static ItemStack getPlayerHead(String name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta sm = (SkullMeta) head.getItemMeta(); sm.setOwner(name); head.setItemMeta(sm);

        return head;
    }
}
