package net.starly.core.util;

import com.google.common.reflect.ClassPath;
import net.starly.core.starlyplugin.StarlyPluginManager;
import net.starly.core.starlyplugin.impl.StarlyPlugin;
import net.starly.core.util.collection.STSet;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public class RegistryUtil {
    @SuppressWarnings("unchecked")
    public static void registerSubPlugins(JavaPlugin plugin, String packageName) {
        try {
            STSet<Class<?>> classes = new STSet<>(
                    ClassPath.from(ClassLoader.getSystemClassLoader())
                            .getAllClasses()
                            .stream()
                            .filter(clazz -> clazz.getName().contains(packageName))
                            .map(ClassPath.ClassInfo::load)
                            .collect(Collectors.toSet())
            );
            classes.forEach(clazz -> {
                if (clazz.getSuperclass() == StarlyPlugin.class) {
                    StarlyPluginManager.registerStarlyPlugin(plugin, (Class<? extends StarlyPlugin>) clazz);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
