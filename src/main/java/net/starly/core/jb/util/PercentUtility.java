package net.starly.core.jb.util;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

public class PercentUtility {

    private static final Random random = new Random();

    /**
     * Key = 객체
     * Value = 무게
     * 위의 값으로 설정 된 맵에서 무게에 따른 랜덤 Key 를 추출해줍니다.
     * @param randomMap Map<객체, 무게(Double)>
     * @return 랜덤으로 뽑힌 객체
     * @param <T> 해당 객체의 타입
     */
    public static <T> T random(Map<T, Double> randomMap) {
        Stream<Map.Entry<T, Double>> entry = randomMap.entrySet().stream();
        return entry
                .map((e)-> new AbstractMap.SimpleEntry<>(e.getKey(), -Math.log(random.nextDouble()) / e.getValue()))
                .min(Comparator.comparingDouble(AbstractMap.SimpleEntry::getValue))
                .orElseThrow(IllegalAccessError::new).getKey();
    }

    /**
     * 확률을 weight random 알고리즘으로 테스트 할 수 있습니다.
     * @param percent 확률 (Double)
     * @return 성공 여부
     */
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

    /**
     * 확률을 weight random 알고리즘으로 테스트 할 수 있습니다.
     * @param percent 확률 (Integer)
     * @return 성공 여부
     */
    public static boolean isSuccess(int percent) {
        return isSuccess((double) percent);
    }

}
