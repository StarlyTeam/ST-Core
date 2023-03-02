package net.starly.core.jb.container.wrapper;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class InventoryClickEventWrapper {
    private final InventoryClickEvent event;
    private boolean buttonCancelled = false;
    public void setCancelled(boolean cancel) { event.setCancelled(cancel); }
    public ItemStack getCurrentItem() {
        return event.getCurrentItem() == null ? new ItemStack(Material.AIR) : event.getCurrentItem();
    }
    public void setButtonCancelled(boolean buttonCancelled) { this.buttonCancelled = buttonCancelled; }
    public boolean isButtonCancelled() { return buttonCancelled; }
    public int getRawSlot() { return event.getRawSlot(); }
    public ClickType getClickType() { return event.getClick(); }
    public Player getPlayer() { return (Player) event.getWhoClicked(); }
    public boolean isCancelled() { return event.isCancelled(); }
    public boolean isShift() { return event.isShiftClick(); }
    public boolean isWheel() { return event.getClick().equals(ClickType.MIDDLE); }
    public int getHotbarButton() { return event.getHotbarButton(); }
}