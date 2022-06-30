package com.practice.chat.utils;

public class TextUtil {
    public static boolean isAccountNumber(String number) {
        if(number.length() != 10) {
            return false;
        } else {
            for(int i = 0; i < 10; ++i) {
                char ch = number.charAt(i);
                if(ch < '0' || ch > '9')
                    return false;
            }
            return true;
        }
    }

}
