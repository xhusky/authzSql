package com.github.authzsql;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Kmx 3A client.
 *
 * @author Think Wong
 */
public class Kmx3AClient {
    private static final String APPLICATION_JSON_UTF8_VALUE = "application/json; charset=utf-8";
    private static OkHttpClient httpClient;

    static {
        httpClient = new OkHttpClient.Builder()
                .readTimeout(30L, TimeUnit.SECONDS)
                .writeTimeout(30L, TimeUnit.SECONDS)
                .connectTimeout(30L, TimeUnit.SECONDS)
                .build();
    }

    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .get()
                .addHeader("Content-Type", APPLICATION_JSON_UTF8_VALUE)
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }


    public static String get(String url, Map<String, String> params) throws IOException {
        String urlParams = buildUrlParams(params);
        String urlGet = urlParams == null ? url : url + '?' + urlParams;
        Request request = new Request.Builder().get().url(urlGet).build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String get(String url, Map<String, String> params, Headers headers) throws IOException {
        return null;
    }


    private static String buildUrlParams(Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            StringBuilder result = new StringBuilder();

            for (Map.Entry entry : params.entrySet()) {
                if (result.length() > 0) {
                    result.append("&");
                }

                result.append(entry.getKey());
                result.append("=");
                result.append(entry.getValue());
            }

            return result.toString();
        } else {
            return null;
        }
    }
}
