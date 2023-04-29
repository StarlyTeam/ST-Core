package net.starly.core.jb.command;

import net.starly.core.jb.annotation.Subcommand;
import net.starly.core.jb.command.wrapper.CommandSenderWrapper;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class STCommand implements CommandExecutor, TabCompleter {

    private final HashMap<String, STSubCommand> subCommands = new HashMap<>();
    private final String command;
    public String getCommand() { return command; }
    private final String description;
    public STCommand(String command, String description, JavaPlugin plugin) {
        this.command = command;
        this.description = description;
        List<Method> methods = Arrays.stream(getClass().getMethods()).filter((method)->method.getReturnType().equals(void.class)).collect(Collectors.toList());
        methods.forEach((it)->
                Arrays.stream(it.getAnnotationsByType(Subcommand.class))
                        .collect(Collectors.toList())
                        .forEach(annotation ->
                                subCommands.put(annotation.subCommand(), new STSubCommand(annotation, it, this))
                        )
        );
        PluginCommand pluginCommand = plugin.getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

    private void command(CommandSenderWrapper sender, String[] args) {
        if (args.length == 0) executeDefaultCommand(sender);
        else {
            String sub = args[0];
            try { subCommands.get(sub).execute(sender, Arrays.copyOfRange(args, 1, args.length)); }
            catch (Exception e) { printHelpLine(sender); }
        }
    }

    public void executeDefaultCommand(CommandSenderWrapper sender) {
        printHelpLine(sender);
    }

    public void printHelpLine(CommandSenderWrapper sender) {
        sender.sendMessage("&6/"+ command + " : &f" + description);
        subCommands.values().forEach((sub)-> {
            if (sub.getAnnotation().isOp() && !sender.isOp()) return;
            sub.printHelpMessage(sender);
        });
    }

    protected List<String> tabComplete(CommandSenderWrapper sender, String[] args) {
        if (args.length <= 1) return StringUtil.copyPartialMatches(args[0], subCommands.entrySet().stream().filter(it->{
            STSubCommand value = it.getValue();
            if (!value.getAnnotation().permission().isEmpty())
                if (!sender.hasPermission(value.getAnnotation().permission())) return false;

            return !(value.getAnnotation().isOp() && !sender.isOp());
        }).map(Map.Entry::getKey).collect(Collectors.toList()), new ArrayList<>());
        else {
            int index = args.length - 2;
            STSubCommand target = subCommands.get(args[0]);
            if (target == null) return null;
            else {
                STArgument<?> tab = target.getArgument(sender, index);
                if (tab == null) return Collections.emptyList();
                else {
                    List<String> result = tab.getTabCompleter().apply();
                    if (result == null) return null;
                    else return StringUtil.copyPartialMatches(args[args.length - 1], result, new ArrayList<>());
                }
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        command(new CommandSenderWrapper(sender), args);
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return tabComplete(new CommandSenderWrapper(sender), args);
    }

}
