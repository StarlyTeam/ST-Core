package net.starly.core.util;

import net.starly.core.starlyplugin.StarlyPluginManager;
import net.starly.core.starlyplugin.impl.StarlyPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.Set;

public class RegistryUtil {
    public static void registerSubPlugin(JavaPlugin plugin, String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends StarlyPlugin>> clazz = reflections.getSubTypesOf(StarlyPlugin.class);

        StarlyPluginManager.registerStarlyPlugin(plugin, new Reflections(packageName).getSubTypesOf(StarlyPlugin.class));
    }
}
