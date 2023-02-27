package net.starly.core.jb.command;

@FunctionalInterface
public interface Castable<T> {
    T cast(String string);
}
