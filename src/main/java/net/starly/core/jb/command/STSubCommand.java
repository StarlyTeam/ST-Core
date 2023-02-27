package net.starly.core.jb.command;

import net.starly.core.StarlyCore;
import net.starly.core.jb.annotation.ArgumentLabel;
import net.starly.core.jb.annotation.NullableArgument;
import net.starly.core.jb.annotation.Subcommand;
import net.starly.core.jb.util.Pair;
import net.starly.core.jb.command.wrapper.CommandSenderWrapper;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class STSubCommand {

    private final ArrayList<Pair<STArgument<?>, Boolean>> arguments = new ArrayList<>();
    private final Subcommand annotation;
    private final Method function;
    private final STCommand parent;
    private final String helpLine;
    public STSubCommand(Subcommand annotation, Method method, STCommand parent) {
        this.annotation = annotation;
        this.function = method;
        this.parent = parent;
        StringBuilder builder = new StringBuilder();
        builder.append("§e/").append(parent.getCommand()).append(" ").append(annotation.subCommand());
        Parameter[] params = method.getParameters();
        for(int i = 0; i < params.length; i++) {
            if(i == 0) continue;
            Parameter param = params[i];
            Class<?> paramClass = param.getType();
            STArgument<?> argument = StarlyCore.getArgumentRepository().getArgument(paramClass);
            if(argument != null) {
                if (param.isAnnotationPresent(NullableArgument.class)) arguments.add(new Pair<>(argument, false));
                else arguments.add(new Pair<>(argument, true));
                String label = argument.getLabel();
                if(param.isAnnotationPresent(ArgumentLabel.class))
                    label = param.getAnnotation(ArgumentLabel.class).value();
                builder.append(" ").append(label);
            }
        }
        helpLine = builder.append(" : §f").append(annotation.description()).toString();
    }

    public Subcommand getAnnotation() { return annotation; }

    public void execute(CommandSenderWrapper sender, String[] args) {
        if(annotation.isOp() && !sender.isOp()) {
            // TODO: throw exception ( Permission Exception )

        } else if(!sender.hasPermission(annotation.permission())) {
            // TODO: throw exception ( Permission Exception )

        } else {
            List<Object> argumentList = new ArrayList<>();
            argumentList.add(sender);
            for(int i = 0; i < arguments.size(); i++) {
                Pair<STArgument<?>, Boolean> it = arguments.get(i);
                Object obj;
                try { obj = it.getFirst().cast(args[i]); } catch (Exception e) {
                    e.printStackTrace();
                    obj = null;
                }
                if(it.getSecond() && obj == null) {
                    // TODO: throw exception ( Null Parameter Exception )
                } else argumentList.add(obj);
            }
            try {
                function.invoke(parent, argumentList.toArray());
            } catch (Exception e) {
                // TODO: throw exception ( Invalid Parameter Exception )
                
            }
        }
    }

    public void printHelpMessage(CommandSenderWrapper sender) { sender.sendMessage(helpLine); }

    public STArgument<?> getArgument(CommandSenderWrapper sender, int index) {
        try {
            if(!sender.isOp() && annotation.isOp()) return null;
            else return arguments.get(index).getFirst();
        } catch (Exception e) { return null; }
    }

}
