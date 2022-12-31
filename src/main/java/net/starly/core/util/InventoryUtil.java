package net.starly.core.util;

import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Objects;

public class InventoryUtil {
    public Boolean hasEnoughSpace(Inventory inventory, Integer currentSpace) {
        return Arrays.stream(inventory.getContents()).filter(Objects::nonNull).toList().size() + currentSpace <= inventory.getSize();
    }

    public Integer getSpace(Inventory inventory) {
        return inventory.getSize() - Arrays.stream(inventory.getContents()).filter(Objects::nonNull).toList().size();
    }
}
