package net.starly.core.util;

import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class InventoryUtil {
    public static Integer getSpace(Inventory inventory) {
        return inventory.getSize() - Arrays.stream(inventory.getContents()).filter(Objects::nonNull).collect(Collectors.toList()).size();
    }
}
