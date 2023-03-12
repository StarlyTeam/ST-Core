package net.starly.core.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class InventoryUtil {
    public static Integer getSpace(Inventory inventory) {
        int filled = (int) Arrays.stream(inventory.getContents()).filter(Objects::nonNull).count();
        if (inventory instanceof PlayerInventory) if (filled > 36) filled = 36;
        return inventory.getSize() - filled;
    }
}
