package com.github.authzsql.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class OkhttpUtils {
    private static OkHttpClient httpClient =
            new OkHttpClient.Builder()
                    .readTimeout(30L, TimeUnit.SECONDS)
                    .writeTimeout(30L, TimeUnit.SECONDS)
                    .connectTimeout(30L, TimeUnit.SECONDS)
                    .build();

    /**
     * get请求
     */
    public static Response get(String url) throws IOException {
        return get(httpClient, url, null);
    }

    /**
     * get请求
     */
    public static Response get(String url, Map<String, String> params) throws IOException {
        return get(httpClient, url, params);
    }

    /**
     * get请求
     */
    public static Response get(OkHttpClient client, String url, Map<String, String> params) throws IOException {
        return createGetCall(client, url, params).execute();
    }

    /**
     * get异步请求
     */
    public static void get(String url, Map<String, String> params, Callback callback) {
        get(httpClient, url, params, callback);
    }

    /**
     * get异步请求
     */
    public static void get(OkHttpClient client, String url, Map<String, String> params, Callback callback) {
        createGetCall(client, url, params).enqueue(callback);
    }

    /**
     * post同步请求
     */
    public static Response post(String url, Headers headers, Map<String, String> params) throws IOException {
        return createPostCall(url, headers, params).execute();
    }

    /**
     * post异步请求
     */
    public static void post(String url, Headers headers, Map<String, String> params, Callback callback) {
        createPostCall(url, headers, params).enqueue(callback);
    }

    /**
     * post同步请求
     */
    public static Response post(String url, Headers headers, Map<String, String> params, Map<String, File> files) throws IOException {
        return createPostCall(url, headers, params, files).execute();
    }

    /**
     * post异步请求
     */
    public static void post(String url, MediaType mediaType, String raw, Callback callback) {
        createPostCall(url, mediaType, raw).enqueue(callback);
    }

    /**
     * post同步请求
     */
    public static Response post(String url, MediaType mediaType, String raw) throws IOException {
        return createPostCall(url, mediaType, raw).execute();
    }

    /**
     * post异步请求
     */
    public static void post(String url, Headers headers, Map<String, String> params, Map<String, File> files, Callback callback) {
        createPostCall(url, headers, params, files).enqueue(callback);
    }

    /**
     * put 同步请求
     */
    public static Response put(String url, MediaType mediaType, String raw) throws IOException {
        return createPutCall(url, mediaType, raw).execute();
    }

    /**
     * put 异步请求
     */
    public static void put(String url, MediaType mediaType, String raw, Callback callback) {
        createPutCall(url, mediaType, raw).enqueue(callback);
    }

    /**
     * 生成 get 请求的 {@link Call} 对象
     */
    private static Call createGetCall(String url, Map<String, String> params) {
        return createGetCall(httpClient, url, params);
    }

    /**
     * 生成 get 请求的 {@link Call} 对象
     */
    private static Call createGetCall(OkHttpClient client, String url, Map<String, String> params) {
        String urlParams = buildUrlParams(params);
        String urlGet = (urlParams == null) ? url : (url + '?' + urlParams);
        Request request = new Request.Builder().get().url(urlGet).build();
        return client.newCall(request);
    }

    /**
     * 生成 get 请求的 url 地址
     */
    private static String buildUrlParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (result.length() > 0)
                result.append("&");
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }
        return result.toString();
    }

    /**
     * 生成 post 请求的 {@link Call} 对象
     */
    private static Call createPostCall(String url, Headers headers, Map<String, String> params, Map<String, File> files) {
        okhttp3.MultipartBody.Builder builder = new MultipartBody.Builder();
        // 上传的参数
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        // 设置上传的文件
        if (files != null && !files.isEmpty()) {

            for (Map.Entry<String, File> entry : files.entrySet()) {
                File file = entry.getValue();
                String contentType = null;

                boolean isPng = file.getName().endsWith(".png") || file.getName().endsWith(".PNG");

                if (isPng) {
                    contentType = "image/png; charset=UTF-8";
                }

                boolean isJpg = file.getName().endsWith(".jpg") || file.getName().endsWith(".JPG")
                        || file.getName().endsWith(".jpeg") || file.getName().endsWith(".JPEG");
                if (isJpg) {
                    contentType = "image/jpeg; charset=UTF-8";
                }
                if (file.exists()) {
                    RequestBody body = RequestBody.create(MediaType.parse(contentType), file);
                    builder.addFormDataPart(entry.getKey(), file.getName(), body);
                }
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).headers(headers).post(requestBody).build();
        return httpClient.newCall(request);
    }

    /**
     * 生成 post 请求的 {@link Call} 对象
     */
    private static Call createPostCall(String url, Headers headers, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.get("query"));
        Request request = new Request.Builder().url(url).headers(headers).post(requestBody).build();
        return httpClient.newCall(request);
    }

    /**
     * 生成 post 请求的 {@link Call} 对象
     */
    private static Call createPostCall(String url, MediaType mediaType, String raw) {
        RequestBody requestBody = RequestBody.create(mediaType, raw);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return httpClient.newCall(request);
    }

    /**
     * 生成 put 请求的 {@link Call} 对象
     */
    private static Call createPutCall(String url, MediaType mediaType, String raw) {
        RequestBody requestBody = RequestBody.create(mediaType, raw);
        Request request = new Request.Builder().url(url).put(requestBody).build();
        return httpClient.newCall(request);
    }
}
