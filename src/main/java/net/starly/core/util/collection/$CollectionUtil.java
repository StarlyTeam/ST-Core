package net.starly.core.util.collection;

import java.util.Arrays;

class $CollectionUtil {

    static boolean castable(Class<?> origin, Class<?> other) {
        return origin == other || Arrays.stream(origin.getGenericInterfaces()).anyMatch(it-> it == other);
    }

}
