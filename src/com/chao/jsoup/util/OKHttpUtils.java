package com.chao.jsoup.util;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * 利用okhttp进行get和post的访问
 * Created by Chao on 2017/8/18.
 */
public class OKHttpUtils {
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * 同步GET
     *
     * @throws Exception
     */
    public static String get(String utl) throws Exception {
        Request request = new Request.Builder()
                .url(utl)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        Headers responseHeaders = response.headers();
        for (int i = 0; i < responseHeaders.size(); i++) {
            //System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
        }
        String string = response.body().string();
        //System.out.println(string);
        return string;
    }

    /**
     * 异步GET
     *
     * @throws Exception
     */
    public static void asynchronousGet(String utl, HttpResponse httpResponse) throws Exception {
        Request request = new Request.Builder()
                .url(utl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.err.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                String string = response.body().string();
                System.out.println(string);
                if (httpResponse != null) {
                    httpResponse.onResponse(string);
                }
            }
        });
    }

    /**
     * 同步POST
     *
     * @throws Exception
     */
    public static String post(String url, Map<String, String> parameter) throws Exception {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : parameter.keySet()) {
            builder.add(key, parameter.get(key));
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String string = response.body().string();
        System.out.println(string);
        return string;
    }

    /**
     * 异步POST
     *
     * @throws Exception
     */
    public static void asynchronousPost(String url, Map<String, String> parameter, HttpResponse httpResponse) throws Exception {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : parameter.keySet()) {
            builder.add(key, parameter.get(key));
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String string = response.body().string();
                System.out.println(string);
                if (httpResponse != null) {
                    httpResponse.onResponse(string);
                }
            }
        });
    }

    public interface HttpResponse {
        public void onResponse(String content);
    }

} 