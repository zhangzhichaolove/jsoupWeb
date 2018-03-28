package com.chao.jsoup.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Chao on 2017/8/18.
 */
public class JsonUtil {
    //    private static Gson gson = new Gson();
    private static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String toJson(Object bean) {
        return gson.toJson(bean);
    }

    public static String toJsonData(Object bean) {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("成功!");
        result.setData(bean);
        return gson.toJson(result);
    }

    public static String toJsonData(int code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return gson.toJson(result);
    }

//    public static <T> String toJsonArray(List<T> bean) {
//        Result result = new Result();
//        result.setCode(200);
//        result.setMsg("成功!");
//        result.setData(bean);
//        return gson.toJson(result);
//    }


}
