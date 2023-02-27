package net.starly.core;

import net.starly.core.jb.repo.STArgumentRepository;
import net.starly.core.jb.repo.impl.$STArgumentRepositoryImpl;
import net.starly.core.jb.util.AsyncExecutor;
import net.starly.core.bstats.Metrics;
import net.starly.core.jb.util.ItemStackNameUtil;
import net.starly.core.jb.util.PlayerSkullManager;
import net.starly.core.jb.version.VersionController;
import net.starly.core.starlyplugin.StarlyPluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class StarlyCore extends JavaPlugin {

    private static $STArgumentRepositoryImpl argumentRepository;

    @Override
    public void onEnable() {
        argumentRepository = new $STArgumentRepositoryImpl();
        new Metrics(this, 17172);

        VersionController.$initializing(this);
        PlayerSkullManager.$initializing(VersionController.getInstance().getVersion(), getServer());
        ItemStackNameUtil.$initializingLocale(this);
    }

    @Override
    public void onDisable() {
        AsyncExecutor.shutdown();
        StarlyPluginManager.unRegisterAllPlugin();
    }

    public static STArgumentRepository getArgumentRepository() { return argumentRepository; }

}