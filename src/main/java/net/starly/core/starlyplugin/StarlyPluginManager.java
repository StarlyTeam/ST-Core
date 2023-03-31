package net.starly.core.starlyplugin;

import net.starly.core.starlyplugin.impl.StarlyPlugin;
import net.starly.core.util.collection.STSet;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;


public class StarlyPluginManager {
    private static final Map<JavaPlugin, STSet<StarlyPlugin>> starlyPlugins = new HashMap<>();

    public static void registerStarlyPlugin(JavaPlugin plugin, Class<? extends StarlyPlugin> clazz) {
        StarlyPlugin instance = null;
        try {
            instance = clazz.newInstance();
        } catch (IllegalAccessException ignored) {
            new IllegalAccessException(clazz.getSimpleName() + "의 객체를 생성할 수 없습니다.").printStackTrace();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        try {
            instance.onRegister();
        } catch (NoSuchMethodError ignored) {
            new IllegalArgumentException(clazz.getSimpleName() + "에 onRegister 메소드가 존재하지 않습니다.").printStackTrace();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        STSet<StarlyPlugin> classes = new STSet<>(starlyPlugins.get(plugin));
        classes.add(instance);

        starlyPlugins.put(plugin, classes);
    }

    public static void registerStarlyPlugin(JavaPlugin plugin, STSet<Class<? extends StarlyPlugin>> classes) {
        classes.forEach(clazz -> {
            try {
                registerStarlyPlugin(plugin, clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void unRegisterAllPlugin() {
        starlyPlugins.keySet().forEach(StarlyPluginManager::unRegisterStarlyPlugin);
    }

    public static void unRegisterStarlyPlugin(JavaPlugin plugin) {
        STSet<StarlyPlugin> instances = starlyPlugins.get(plugin);
        instances.forEach(StarlyPlugin::onUnRegister);

        starlyPlugins.remove(plugin);
    }
}
