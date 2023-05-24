package net.starly.core.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class InventoryUtil {
    public static Integer getSpace(Inventory inventory) {
        int filled = (int) Arrays.stream(inventory.getContents()).filter(Objects::nonNull).count();
        if (inventory instanceof PlayerInventory) {
            filled -= Arrays.stream(((PlayerInventory) inventory).getArmorContents()).filter(Objects::nonNull).count();
            if (((PlayerInventory) inventory).getItemInOffHand().getType() != Material.AIR) filled--;
        }
        return inventory.getSize() - filled;
    }

    public static boolean removeItem(Player player, ItemStack itemStack, int amount) { // TODO : FIX BUGS
        Map<Integer, ? extends ItemStack> matchSlots = player.getInventory().all(itemStack.getType());
        new HashMap<>(matchSlots).forEach((slot, currentStack) -> {
            if (slot == null || currentStack == null) return;
            if (!currentStack.isSimilar(itemStack)) matchSlots.remove(slot, currentStack);
        });
        Arrays.asList(100, 101, 102, 103, -106).forEach(matchSlots::remove);

        int matchAmount = 0;
        for (ItemStack stack : matchSlots.values()) matchAmount += stack.getAmount();
        if (amount > matchAmount) return false;

        for (Integer slot : matchSlots.keySet()) {
            ItemStack slotStack = matchSlots.get(slot);

            int removed = Math.min(amount, slotStack.getAmount());
            amount -= removed;

            if (slotStack.getAmount() == removed) {
                player.getInventory().setItem(slot, null);
            } else {
                slotStack.setAmount(slotStack.getAmount() - removed);
            }

            if (amount <= 0) break;
        }

        player.updateInventory();
        return true;
    }
}
