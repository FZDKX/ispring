package com.fzdkx.spring.util;

/**
 * @author 发着呆看星
 * @create 2024/8/11
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static String lowerFirst(String str) {
        char firstChar = str.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            return str;
        }
        return Character.toLowerCase(firstChar) + str.substring(1);
    }
}
