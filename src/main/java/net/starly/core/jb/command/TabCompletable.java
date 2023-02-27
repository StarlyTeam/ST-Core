package net.starly.core.jb.command;

import java.util.List;

@FunctionalInterface
public interface TabCompletable {
    List<String> apply();
}
