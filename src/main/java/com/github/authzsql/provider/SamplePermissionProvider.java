package com.github.authzsql.provider;

import com.github.authzsql.model.Permission;
import com.github.authzsql.utils.SqlPermissionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class SamplePermissionProvider implements PermissionsProvider {

    private static Map<String, Map<String, List<Permission>>> resourceMap;

    static {
        List<Permission> permissions = new ArrayList<>();
        Permission permission = new Permission();
        permission.setResourceType("md4x/windfarm");
        permission.setResourceInfo("LIKE:100040%");
        permission.setOperation("EDIT");
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
        permission21.setResourceInfo("IN:100070");
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


    @Override
    public Set<String> operations(String resourceType) {
        resourceType = SqlPermissionHelper.fillResourceType(resourceType);

        Map<String, List<Permission>> operationPermissionMap = resourceMap.get(resourceType);
        if (operationPermissionMap != null) {
            return operationPermissionMap.keySet();
        }

        return Collections.emptySet();
    }

    @Override
    public List<Permission> permissions(String resourceType, String operation) {
        resourceType = SqlPermissionHelper.fillResourceType(resourceType);
        Map<String, Map<String, List<Permission>>> permissionMap = resourceMap;
        if (permissionMap != null && permissionMap.get(resourceType) != null
                && permissionMap.get(resourceType).get(operation) != null) {
            return permissionMap.get(resourceType).get(operation);
        }

        return Collections.emptyList();
    }
}