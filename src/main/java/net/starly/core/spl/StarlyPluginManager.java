package net.starly.core.spl;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StarlyPluginManager {
    private static Map<JavaPlugin, List<StarlyPlugin>> plugins = new HashMap<>();


    public static void registerSubPlugin(JavaPlugin plugin, StarlyPlugin subPlugin) {
        List<StarlyPlugin> subPlugins = plugins.getOrDefault(plugin, new ArrayList<>());
        subPlugins.add(subPlugin);
        plugins.put(plugin, subPlugins);

        subPlugin.onRegister();
    }

    public static void unregisterSubPlugin(JavaPlugin plugin, StarlyPlugin subPlugin) {
        subPlugin.onUnregister();

        List<StarlyPlugin> subPlugins = plugins.getOrDefault(plugin, new ArrayList<>());
        subPlugins.remove(subPlugin);
        plugins.put(plugin, subPlugins);
    }

    public static void unregisterAllSubPlugins() {
        for (JavaPlugin plugin : plugins.keySet()) {
            for (StarlyPlugin subPlugin : plugins.get(plugin)) {
                subPlugin.onUnregister();
            }
        }

        plugins = new HashMap<>();
    }

    public static void unregisterAllSubPlugins(JavaPlugin plugin) {
        List<StarlyPlugin> subPlugins = plugins.getOrDefault(plugin, new ArrayList<>());
        for (StarlyPlugin subPlugin : subPlugins) {
            subPlugin.onUnregister();
        }
        plugins.remove(plugin);
    }
}
