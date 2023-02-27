package net.starly.core.jb.container.wrapper;

import lombok.AllArgsConstructor;
import net.starly.core.jb.container.button.STButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class ButtonClickEventWrapper {
    private final InventoryClickEvent event;
    private final STButton button;
    public ItemStack getItemStack() {
        return event.getCurrentItem() == null ? new ItemStack(Material.AIR) : event.getCurrentItem();
    }
    public String getDisplayName() { return button.getDisplayName(); }
    public List<String> getLore() { return button.getLore(); }
    public boolean isGlow() { return button.isGlow(); }
    public boolean isCleanable() { return button.isCleanable(); }
    public int getButtonSlot() { return event.getRawSlot(); }
    public ClickType getClickType() { return event.getClick(); }
    public Player getPlayer() { return (Player) event.getWhoClicked(); }
    public boolean isCancelled() { return event.isCancelled(); }
    public boolean isShift() { return event.isShiftClick(); }
    public boolean isWheel() { return event.getClick().equals(ClickType.MIDDLE); }
    public boolean isLeft() { return event.isLeftClick(); }
    public boolean isRight() { return event.isRightClick(); }
    public boolean isShiftRight() { return event.getClick().equals(ClickType.SHIFT_RIGHT); }
    public boolean isShiftLeft() { return event.getClick().equals(ClickType.SHIFT_LEFT); }
    public boolean pressQButton() { return event.getClick().equals(ClickType.DROP); }
}