package net.starly.core.util;

import net.starly.core.starlyplugin.StarlyPluginManager;
import net.starly.core.starlyplugin.impl.StarlyPlugin;
import net.starly.core.util.collection.STSet;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

public class RegistryUtil {
    public static void registerSubPlugins(JavaPlugin plugin, String packageName) {
        StarlyPluginManager.registerStarlyPlugin(plugin, new STSet<>(new Reflections(packageName).getSubTypesOf(StarlyPlugin.class)));
    }
}
