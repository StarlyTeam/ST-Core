package net.starly.core.starlyplugin;

import net.starly.core.starlyplugin.impl.StarlyPlugin;
import net.starly.core.util.collection.STSet;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;


public class StarlyPluginManager {
    private static final Map<JavaPlugin, STSet<StarlyPlugin>> REGISTERED_PLUGINS = new HashMap<>();

    public static void registerStarlyPlugin(JavaPlugin plugin, Class<? extends StarlyPlugin> clazz) {
        StarlyPlugin instance;
        try { instance = clazz.newInstance(); }
        catch (IllegalAccessException ignored) {
            new IllegalAccessException(clazz.getSimpleName() + "의 객체를 생성할 수 없습니다.").printStackTrace();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        try { instance.onRegister(); }
        catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        STSet<StarlyPlugin> classes = REGISTERED_PLUGINS.get(plugin);
        classes.add(instance);
        REGISTERED_PLUGINS.put(plugin, classes);
    }

    public static void registerStarlyPlugin(JavaPlugin plugin, STSet<Class<? extends StarlyPlugin>> classes) {
        classes.forEach(clazz -> registerStarlyPlugin(plugin, clazz));
    }

    public static void unRegisterAllPlugin() {
        REGISTERED_PLUGINS.keySet().forEach(StarlyPluginManager::unRegisterStarlyPlugin);
    }

    public static void unRegisterStarlyPlugin(JavaPlugin plugin) {
        if (REGISTERED_PLUGINS.containsKey(plugin)) {
            REGISTERED_PLUGINS.get(plugin).forEach(StarlyPlugin::onUnRegister);
            REGISTERED_PLUGINS.remove(plugin);
        }
    }
}
