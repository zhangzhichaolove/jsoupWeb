package com.chao.jsoup.util;

import okhttp3.*;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.util.Map;

/**
 * 利用okhttp进行get和post的访问
 * Created by Chao on 2017/8/18.
 */
public class OKHttpUtils {
    private static final OkHttpClient client = new OkHttpClient();
    private static final OkHttpClient mClient;

    static {
        Interceptor mTokenInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request authorised = originalRequest.newBuilder()
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.9")
                        .header("Cache-Control", "max-age=0")
                        .header("Connection", "keep-alive")
                        .header("Host", "api.budejie.com")
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36")
                        //Mozilla/5.0 (Linux; U; Android 7.0; zh-CN; MI 5 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.4.1.939 UWS/2.11.0.8 Mobile Safari/537.36 Shuqi (Xiaomi-MI 5__shuqi__10.6.7.64__1075)
                        .build();
                return chain.proceed(authorised);
            }
        };
        mClient = new OkHttpClient.Builder().addNetworkInterceptor(mTokenInterceptor).build();
    }


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
     * 同步GET带请求头
     *
     * @throws Exception
     */
    public static String getWeb(String utl) throws Exception {
        Request request = new Request.Builder()
                .url(utl)
                //Mozilla/5.0 (Linux; U; Android 7.0; zh-CN; MI 5 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.4.1.939 UWS/2.11.0.8 Mobile Safari/537.36 Shuqi (Xiaomi-MI 5__shuqi__10.6.7.64__1075)
                .build();

        Response response = mClient.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
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