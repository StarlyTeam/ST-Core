package net.starly.core.util;

public class PreCondition {

    private PreCondition() {}

    public static void nonNull(Object object, String message) {
        if (object == null) throw new NullPointerException(message);
    }
}
