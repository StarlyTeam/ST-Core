package net.starly.core.jb.command;

import net.starly.core.jb.util.AsyncExecutor;
import org.jetbrains.annotations.NotNull;

public class STArgument<T> implements Comparable<STArgument> {

    public STArgument(Class<T> clazz, String label, Castable<T> castable) { this(clazz, label, castable,false); }
    public STArgument(Class<T> clazz, String label, Castable<T> castable, int priority) { this(clazz, label, castable,false, priority); }
    public STArgument(Class<T> clazz, String label, Castable<T> castable, TabCompletable tabCompletable) { this(clazz, label, castable,tabCompletable, false, 0); }
    public STArgument(Class<T> clazz, String label, Castable<T> castable, boolean async) { this(clazz, label, castable, ()->null, async, 0); }
    public STArgument(Class<T> clazz, String label, Castable<T> castable, boolean async, int priority) { this(clazz, label, castable, ()->null, async, priority); }

    private final Class<T> clazz;
    private final String label;
    private final Castable<T> castable;
    private final TabCompletable tabCompletable;
    private final boolean async;
    private final int priority;
    public STArgument(Class<T> clazz, String label, Castable<T> castable, TabCompletable tabCompletable, boolean async, int priority) {
        this.clazz = clazz;
        this.label = label;
        this.castable = castable;
        this.tabCompletable = tabCompletable;
        this.async = async;
        if (priority < 0) priority = 0;
        this.priority = priority;
    }

    public String getArgumentClassName() {
        return clazz.getSimpleName();
    }

    public String getLabel() {
        return label;
    }

    public T cast(String buf) {
        if (async) return AsyncExecutor.submit(()->castable.cast(buf));
        else return castable.cast(buf);
    }

    @Override
    public int compareTo(@NotNull STArgument o) {
        return Integer.compare(priority, o.priority);
    }

    public TabCompletable getTabCompleter() {
        return tabCompletable;
    }
}
