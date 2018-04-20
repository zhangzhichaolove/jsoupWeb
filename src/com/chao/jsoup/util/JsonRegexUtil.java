package com.chao.jsoup.util;

import java.util.regex.Pattern;

/**
 * Created by 003 on 2018/4/20.
 */
public class JsonRegexUtil {

    public static boolean isJson(String json) {
        String regex = "[\\{\\[]+.*[\\}\\]]+";
        return Pattern.matches(regex, json);
//        Matcher matcher = Pattern.compile(regex).matcher(json);
//        while (matcher.find()) {
//            String ret = matcher.group();
//            System.out.println(ret);
//        }
    }
}
