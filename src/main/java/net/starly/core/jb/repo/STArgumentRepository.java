package net.starly.core.jb.repo;

import net.starly.core.jb.command.STArgument;

public interface STArgumentRepository {

    void registerArguments(STArgument<?>... argument);
    STArgument<?> getArgument(Class<?> clazz);

}
