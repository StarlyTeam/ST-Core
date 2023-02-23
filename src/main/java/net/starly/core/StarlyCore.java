package net.starly.core;

import net.starly.core.bstats.Metrics;
import net.starly.core.starlyplugin.StarlyPluginManager;
import net.starly.core.util.LicenseUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class StarlyCore extends JavaPlugin {
    @Override
    public void onEnable() {
        new Metrics(this, 17172);
        LicenseUtil.checkLicense(this, "831983e6-e4bf-4ca9-afd1-3cde282d269b");
    }

    @Override
    public void onDisable() {
        StarlyPluginManager.unRegisterAllPlugin();
    }
}