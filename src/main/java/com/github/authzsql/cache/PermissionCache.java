package com.github.authzsql.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.github.authzsql.exception.CacheException;
import com.github.authzsql.model.Permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class PermissionCache {

    // TODO 登录获取 登出清空
    private static final String RESOURCE_TYPE_PREFIX = "md4x/";
    private static Map<String, Map<String, List<Permission>>> resourceMap;
    private static LoadingCache<String, Map<String, Map<String, List<Permission>>>> cache = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, Map<String, Map<String, List<Permission>>>>() {
                @Override
                public Map<String, Map<String, List<Permission>>> load(String key) throws Exception {
                    return handlePermission(key);
                }
            });

    static {
        List<Permission> permissions = new ArrayList<>();
        Permission permission = new Permission();
        permission.setResourceType("md4x/windfarm");
        permission.setResourceInfo("LIKE:100040%");
        permission.setOperation("VIEW");
        permissions.add(permission);

        Permission permission1 = new Permission();
        permission1.setResourceType("md4x/windfarm");
        permission1.setResourceInfo("LIKE:100050%");
        permission1.setOperation("VIEW");
        permissions.add(permission1);

        Permission permission2 = new Permission();
        permission2.setResourceType("md4x/windfarm");
        permission2.setResourceInfo("NOT_LIKE:100060%");
        permission2.setOperation("VIEW");
        permissions.add(permission2);

        Permission permission21 = new Permission();
        permission21.setResourceType("md4x/windfarm");
        permission21.setResourceInfo("LIKE:100070%");
        permission21.setOperation("VIEW");
        permissions.add(permission21);

        Permission permission22 = new Permission();
        permission22.setResourceType("md4x/windfarm");
        permission22.setResourceInfo("*");
        permission22.setOperation("VIEW");
        permissions.add(permission22);

        resourceMap = new HashMap<>();
        for (Permission perm : permissions) {
            String resourceType = perm.getResourceType();
            String operation = perm.getOperation().toUpperCase();

            resourceMap.putIfAbsent(resourceType, new HashMap<>());
            resourceMap.get(resourceType).putIfAbsent(operation, new ArrayList<>());
            resourceMap.get(resourceType).get(operation).add(perm);
        }
    }

    public static Map<String, Map<String, List<Permission>>> get(String key) {
        try {
            return cache.get(key);
        } catch (ExecutionException ex) {
            throw new CacheException(ex);
        }
    }

    public static void invildate(String key) {
        cache.invalidate(key);
    }

    private static Map<String, Map<String, List<Permission>>> handlePermission(String key) {
        return resourceMap;
    }

    public static List<Permission> permissions(String resourceType, String operation) {
        resourceType = RESOURCE_TYPE_PREFIX + resourceType;
        if (resourceMap != null && resourceMap.get(resourceType) != null && resourceMap.get(resourceType).get(operation) != null) {
            return resourceMap.get(resourceType).get(operation);
        }

        return Collections.emptyList();
    }

}
