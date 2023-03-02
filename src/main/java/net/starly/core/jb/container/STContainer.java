package net.starly.core.jb.container;

import net.starly.core.StarlyCore;
import net.starly.core.jb.container.button.STButton;
import net.starly.core.jb.container.wrapper.ButtonClickEventWrapper;
import net.starly.core.jb.container.wrapper.InventoryClickEventWrapper;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class STContainer implements InventoryHolder {

    private static final List<String> viewers = new ArrayList<>();
    private static final StarlyCore plugin;
    private static final Server server;
    static {
        plugin = StarlyCore.getInstance();
        server = plugin.getServer();
    }
    private static void registerPlayer(Player player) { viewers.add(player.getName()); }
    private static void unregisterPlayer(Player player) { viewers.remove(player.getName()); }
    public static void closeAll() {
        viewers.forEach(STContainer::closeFunc);
    }

    private static void closeFunc(String playerName) {
        Player target = server.getPlayer(playerName);
        if(target != null) {
            target.closeInventory();
        } else viewers.remove(playerName);
    }

    protected Inventory inventory;
    protected Player viewer;

    @Override public Inventory getInventory() { return inventory; }

    private final HashMap<Integer, STButton> slotMap = new HashMap<>();
    private InventoryType type;
    private final int size;
    private final String title;
    private final boolean cancel;
    public STContainer(InventoryType type, String title, boolean cancel) {
        this(-1, title, cancel);
        this.type = type;
        createInventory();
    }

    public STContainer(int size, String title, boolean cancel) {
        type = null;
        this.cancel = cancel;
        this.size = size;
        this.title = title;
        createInventory();
    }

    private void createInventory() {
        Server server = StarlyCore.getInstance().getServer();
        inventory = type == null ? server.createInventory(this, size, title) : server.createInventory(this, type, title);
    }

    public void registerButton(int slot, STButton button) {
        if(slotMap.containsKey(slot)) {
            //STButton old = slotMap.get(slot);
            ItemStack oldItem = inventory.getItem(slot);
            oldItem.setType(button.getOriginalItemStack().getType());
            oldItem.setDurability(button.getOriginalItemStack().getDurability());
            oldItem.setAmount(button.getOriginalItemStack().getAmount());
            oldItem.setItemMeta(button.getOriginalItemStack().getItemMeta());
            return;
        }
        slotMap.put(slot, button);
        inventory.setItem(slot, button.getItemStack());
    }

    public void refresh() {
        for(int i = 0; i < size; i++) {
            if(slotMap.containsKey(i)) {
                if (!slotMap.get(i).isCleanable()) continue;
            }
            inventory.setItem(i, null);
            slotMap.remove(i);
        }
        initializingInventory(inventory);
        openedInitializing();
    }

    public void open(Player player) {
        if(player == null || !player.isOnline()) return;
        try {
            viewer = player;
            initializingInventory(inventory);
            plugin.getServer().getScheduler().runTaskLater(plugin, ()->{
                InventoryView openInventory = player.getOpenInventory();
                if(!openInventory.getType().equals(InventoryType.PLAYER) && !openInventory.getType().equals(InventoryType.CREATIVE)) {
                    player.closeInventory();
                    plugin.getServer().getScheduler().runTaskLater(plugin, () ->  open(player), 1L);
                } else {
                    openedInitializing();
                    player.openInventory(inventory);
                    registerPlayer(player);
                }
            }, 1L);
        } catch (Exception e) { player.closeInventory(); }
    }

    protected void openedInitializing() {}

    public void $click(InventoryClickEvent event) {
        if(cancel) event.setCancelled(true);
        InventoryClickEventWrapper wrapper = new InventoryClickEventWrapper(event, false);
        guiClick(wrapper);
        ItemStack itemChecker = event.getCurrentItem();
        if(itemChecker == null || itemChecker.getType().equals(Material.AIR)) return;
        if(!wrapper.isButtonCancelled() && slotMap.containsKey(wrapper.getRawSlot())) {
            STButton button = slotMap.get(wrapper.getRawSlot());
            button.execute(this, new ButtonClickEventWrapper(event, button));
            if(button.isCancelled()) event.setCancelled(true);
        }
    }
    public void $close(InventoryCloseEvent event) {
        unregisterPlayer((Player) event.getPlayer());
        guiClose(event);
    }

    public void $drag(InventoryDragEvent event) { guiDrag(event); }

    protected abstract void guiClick(InventoryClickEventWrapper event);
    protected abstract void guiClose(InventoryCloseEvent event);
    protected abstract void guiDrag(InventoryDragEvent event);
    protected abstract void initializingInventory(Inventory inventory);

}
