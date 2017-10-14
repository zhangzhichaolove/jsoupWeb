package com.chao.jsoup;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Chao on 2017/10/11.
 */
public class HttpTool {

    public static String doGet(String urlStr) throws CommonException {
        URL url;
        String html = "";
        try {
            url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            if (connection.getResponseCode() == 200) {
                InputStream in = connection.getInputStream();
                html = StreamTool.inToStringByByte(in);
            } else {
                throw new CommonException("服务器返回值不为200");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("get请求失败");
        }
        return html;
    }
}
