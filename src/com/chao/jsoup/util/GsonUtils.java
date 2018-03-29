package com.chao.jsoup.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 003 on 2018/3/29.
 */
public class GsonUtils {
    private static Gson gson;
    private static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();

            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    //设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
                    //sd.setLenient(false);
                    String time = json.getAsString();
                    Date date = null;
                    //Pattern.matches(pattern, content)
                    if (time.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}") || time.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        try {
                            date = sd.parse(time);
                        } catch (ParseException e) {
                            date = new Date();
                            e.printStackTrace();
                        }
                    } else if (time.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}") || time.matches("\\d{4}/\\d{2}/\\d{2}")) {
                        try {
                            date = sdf.parse(time);
                        } catch (ParseException e) {
                            date = new Date();
                            e.printStackTrace();
                        }
                    } else if (time.matches("\\d{10,13}")) {
                        date = new Date(json.getAsJsonPrimitive().getAsLong());
                    } else if (time.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        date = new Date(json.getAsJsonPrimitive().getAsLong());
                    } else {
                        date = new Date();
                    }
                    return date;
                }
            });

            gson = builder.create();
        }
        return gson;
    }

}
