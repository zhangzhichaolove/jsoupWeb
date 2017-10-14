package com.chao.jsoup;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamTool {
    public static String inToStringByByte(InputStream in) throws Exception {
//            ByteArrayOutputStream outStr = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int len = 0;
//        StringBuilder content = new StringBuilder();
//        while ((len = in.read(buffer)) != -1) {
//            content.append(new String(buffer, 0, len, "UTF-8"));
//        }
//            outStr.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        return content.toString();
    }
}