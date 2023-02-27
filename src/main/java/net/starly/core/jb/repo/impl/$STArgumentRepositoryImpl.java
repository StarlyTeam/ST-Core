package net.starly.core.jb.repo.impl;

import net.starly.core.jb.command.STArgument;
import net.starly.core.jb.repo.STArgumentRepository;
import net.starly.core.jb.util.AsyncExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class $STArgumentRepositoryImpl implements STArgumentRepository {

    private final HashMap<String, List<STArgument<?>>> argumentMap;

    public $STArgumentRepositoryImpl() {
        argumentMap = new HashMap<>();
        registerArguments(
                new STArgument<>(Player.class, "플레이어", (buf) -> Bukkit.getServer().getPlayer(buf), () -> null, false, 0),
                new STArgument<>(Integer.class, "정수(int)", (buf) -> {
                    try { return Integer.parseInt(buf); } catch (NumberFormatException ignored) { return null; }
                }, () -> null, false, 0),
                new STArgument<>(Long.class, "정수(long)", (buf) -> {
                    try { return Long.parseLong(buf); } catch (NumberFormatException ignored) { return null; }
                }, () -> null, false, 0),
                new STArgument<>(Double.class, "실수", (buf) -> {
                    try { return Double.parseDouble(buf); } catch (NumberFormatException ignored) { return null; }
                }, () -> null, false, 0),
                new STArgument<>(String.class, "문자열", (buf) -> buf, () -> null, false, 0)
        );
    }

    @Override public void registerArguments(STArgument<?>... arguments) {
        for(STArgument<?> argument : arguments)
            argumentMap.computeIfAbsent(argument.getArgumentClassName(), (unused)->new ArrayList<>()).add(argument);
        sortAsync();
    }

    @Override
    public STArgument<?> getArgument(Class<?> clazz) {
        if(argumentMap.containsKey(clazz.getSimpleName())) {
            List<STArgument<?>> list = argumentMap.get(clazz.getSimpleName());
            if(list.isEmpty()) return null;
            else return list.get(0);
        }
        return null;
    }

    private void sortAsync() {
        AsyncExecutor.run(()-> argumentMap.forEach((unused, value)-> value.sort(STArgument::compareTo)));
    }
}
