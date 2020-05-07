package com.daizhihua.sample.util;

public class NumberUtil {

    public static String toNumber(String str) {
        String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] s2 = { "0",  "1",  "2",  "3", "4",  "5",  "6", "7", "8", "9" };
        String result = "";
        for (int i = 0; i < s1.length; i++) {
            if(str.equals(s1[i])){
                return s2[i];
            }
        }


        return result;
    }
}
