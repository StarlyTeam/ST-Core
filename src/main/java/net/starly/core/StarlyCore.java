package net.starly.core;

import net.starly.core.bstats.Metrics;
import net.starly.core.data.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class StarlyCore extends JavaPlugin {
    public Config config;

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 17172);
    }
}