package net.starly.core;

import net.starly.core.bstats.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class StarlyCore extends JavaPlugin {
    @Override
    public void onEnable() {
        new Metrics(this, 17172);
    }
}