package com.gas.utils;

public class StringUtil {
    /**
     * 去除字符中空格
     * @param string
     * @return
     */
    public static String eliminateStringBlank(String string) {
        return string.replace(" ", "");
    }

    /**
     * 判断字符串是否为空 或者 长度为0
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        if (string != null) {
            string = eliminateStringBlank(string);
            return !(string.length() == 0);
        }
        return false;
    }
}
