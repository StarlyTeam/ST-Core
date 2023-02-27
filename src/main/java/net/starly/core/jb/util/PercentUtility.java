package net.starly.core.jb.util;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

public class PercentUtility {

    private static final Random random = new Random();
    public static <T> T random(Map<T, Double> randomMap) {
        Stream<Map.Entry<T, Double>> entry = randomMap.entrySet().stream();
        return entry
                .map((e)-> new AbstractMap.SimpleEntry<>(e.getKey(), -Math.log(random.nextDouble()) / e.getValue()))
                .min(Comparator.comparingDouble(AbstractMap.SimpleEntry::getValue))
                .orElseThrow(IllegalAccessError::new).getKey();
    }

    public static boolean isSuccess(double percent) {
        if(percent >= 100.0) return true;
        else if(percent <= 0.0) return false;
        else {
            BigDecimal dec = new BigDecimal(percent);
            BigDecimal fail = new BigDecimal(100);
            fail = fail.subtract(dec);
            Map<Boolean, Double> map = new HashMap<>();
            map.put(false, fail.doubleValue());
            map.put(true, dec.doubleValue());
            return random(map);
        }
    }

    public static boolean isSuccess(int percent) {
        return isSuccess((double) percent);
    }

}
