package org.example.recruit.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: 22866
 * @date: 2023/11/1
 **/
public class StringUtil {

    /**
     * 判断字符串 s 是否为空 或者为null
     * @param s
     * @return
     */
    public static boolean isEmpty(String s){
        return s == null || s.isEmpty() || s.equals("null");
    }
    public static boolean stateIsNull(int s){
        return s ==0 || s==1;
    }//getIsInoculation
    public static boolean isInoculationIsNull(int s){
        return s ==0 || s==1;
    }//get
    public static boolean isNullOrNullStr(String s){
        return s == null || s.equals("null");
    }


    public static final String convertStreamToString(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            // 处理异常
            return null;
        }
    }
}
