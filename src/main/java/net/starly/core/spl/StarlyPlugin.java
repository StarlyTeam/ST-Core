package net.starly.core.spl;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class StarlyPlugin {
    public void register() {
        StarlyPluginManager.registerSubPlugin(JavaPlugin.getProvidingPlugin(getClass()), this);
    }

    public void onRegister() {}
    public void onUnregister() {}
}
