package net.starly.core.jb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subcommand {
    /**
     * @return SubCommand
     */
    String subCommand();

    /**
     * @return Description
     */
    String description() default "";

    /**
     * @return isOpCommand
     */
    boolean isOp() default false;

    /**
     * @return Permission
     */
    String permission() default "";

    /**
     * @return Priority
     */
    int priority() default 0;
}
