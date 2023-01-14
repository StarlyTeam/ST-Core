package net.starly.core;

import net.starly.core.spl.StarlyPluginManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StarlyCore extends JavaPlugin implements Listener {
    @Override public void onDisable() {
        StarlyPluginManager.unregisterAllSubPlugins();
    }

    @EventHandler public void onPluginDisable(PluginDisableEvent e) {
        StarlyPluginManager.unregisterAllSubPlugins((JavaPlugin) e.getPlugin());
    }
}