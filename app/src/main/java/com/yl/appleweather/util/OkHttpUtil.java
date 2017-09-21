package com.yl.appleweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 网络访问工具类
 * Created by Luke on 2017/9/18.
 */

public class OkHttpUtil {
    public static void sendOkHttpRequest(
            String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
