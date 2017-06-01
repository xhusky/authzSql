package com.github.authzsql.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.github.authzsql.exception.CacheException;

import java.util.concurrent.ExecutionException;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class PermissionCache {

    // TODO 登录获取 登出清空

    private static LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return handlePermission(key);
                }
            });

    public static String get(String key) {
        try {
            return cache.get(key);
        } catch (ExecutionException ex) {
            throw new CacheException(ex);
        }
    }

    public static void invildate(String key) {
        cache.invalidate(key);
    }

    private static String handlePermission(String key) {
        return key;
    }

}
