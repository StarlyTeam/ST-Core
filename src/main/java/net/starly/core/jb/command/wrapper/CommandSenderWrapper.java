package net.starly.core.jb.command.wrapper;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class CommandSenderWrapper {

    private final CommandSender sender;
    private final boolean isPlayer;
    private final boolean isOp;

    public CommandSenderWrapper(CommandSender sender) {
        this.sender = sender;
        isPlayer = sender instanceof Player;
        isOp = sender.isOp();
    }

    public boolean hasPermission(String permission) {
        if (permission.isEmpty()) return true;
        return sender.hasPermission(permission);
    }

    public boolean isOp() {
        return isOp;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Player getPlayer() {
        return isPlayer? (Player) sender : null;
    }

    public ConsoleCommandSender getConsoleSender() {
        return isPlayer? null : (ConsoleCommandSender) sender;
    }

    public void sendMessage(String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendMessages(String... messages) {
        Arrays.stream(messages).forEach(this::sendMessages);
    }

    public void teleport(Location location) {
        if (isPlayer) getPlayer().teleport(location);
    }

    public void openInventory(Inventory inventory) {
        if (isPlayer) getPlayer().openInventory(inventory);
    }

    public void performCommand(String command) {
        performCommand(command, false);
    }

    public void performCommand(String command, boolean setOp) {
        if (!isPlayer) return;
        boolean op = isOp;
        try {
            sender.setOp(setOp);
            getPlayer().performCommand(command);
        } finally { if (!op) sender.setOp(false); }
    }

}
