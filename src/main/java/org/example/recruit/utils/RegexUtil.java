package org.example.recruit.utils;

/**
 * @description:
 * @author: 22866
 * @date: 2023/11/6
 **/
public class RegexUtil {
    public static boolean isPhoneValid(String phone){
        String regex = "^1[3456789]\\d{9}$";
        boolean isMatches = phone.matches(regex);
        return isMatches;
    }
}
