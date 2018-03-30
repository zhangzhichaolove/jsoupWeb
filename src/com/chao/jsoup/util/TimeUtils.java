package com.chao.jsoup.util;

import java.text.SimpleDateFormat;

/**
 * Created by 003 on 2018/3/30.
 */
public class TimeUtils {
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat getFormatter() {
        return formatter;
    }
}
