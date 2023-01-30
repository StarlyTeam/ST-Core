package net.starly.core.util;

public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean containsSpecialChar(String str) {
        if (isEmpty(str)) {
            return false;
        }

        return str.replaceAll("[a-zA-Zㄱ-ㅎ가-힣0-9]", "").length() != 0;
    }

    public static boolean isNumber(String str) {
        if (isEmpty(str)) {
            return false;
        }

        return str.replaceAll("[0-9]", "").length() != 0;
    }
}
