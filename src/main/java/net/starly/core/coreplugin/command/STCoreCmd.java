package net.starly.core.coreplugin.command;

import net.starly.core.StarlyCore;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class STCoreCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("§c§l!!!! §6This command is for DEVELOPERS. §c§l!!!!");

        if (args.length == 0) return true;
        switch (args[0]) {
            case "scan": {
                StringBuilder sb = new StringBuilder();

                Properties properties = System.getProperties();
                Server server = StarlyCore.getInstance().getServer();


                sb.append("# Java Information")
                        .append("\nJava Version: ").append(properties.getProperty("java.version"))
                        .append("\nJava VM Vendor: ").append(properties.getProperty("java.vm.vendor"))
                        .append("\nJava VM Version: ").append(properties.getProperty("java.vm.version"))
                        .append("\nJava Class Version: ").append(properties.getProperty("java.class.version"))
                        .append("\nJava Runtime Version: ").append(properties.getProperty("java.runtime.version"))
                        .append("\nJava Specification Vendor: ").append(properties.getProperty("java.specification.vendor"))
                        .append("\nJava Specification Version: ").append(properties.getProperty("java.specification.version"))
                        .append("\nJava VM Specification Version: ").append(properties.getProperty("java.vm.specification.version"));

                sb.append("\n=========================\n# System Information")
                        .append("\nOperating System: ").append(properties.getProperty("os.name"));

                sb.append("\n=========================\n# Bukkit Information")
                        .append("\nBukkit Version: ").append(server.getVersion())
                        .append("\nBukkit Name: ").append(server.getName())
                        .append("\nNMS Version: ").append(server.getBukkitVersion())
                        .append("\nPlugins: ").append(
                                Arrays.stream(
                                                server
                                                        .getPluginManager()
                                                        .getPlugins())
                                        .map(plugin -> (plugin.isEnabled() ? "§a" : "§c") + plugin.getName())
                                        .collect(Collectors.joining("§r, ")
                                        )
                        );

                sender.sendMessage(sb.toString());
                return true;
            }

            case "config": {
                File configFile = new File(StarlyCore.getInstance().getDataFolder().getParent(), args[1]);
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

                sender.sendMessage(args[1] + " Report");
                sender.sendMessage("=========================");
                config.getKeys(true).forEach(key -> sender.sendMessage(key + ": " + config.getString(key)));
                return true;
            }

            default: {
                sender.sendMessage("Unknown Command.");
                return true;
            }
        }
    }
}
