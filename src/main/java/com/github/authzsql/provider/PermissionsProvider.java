package com.github.authzsql.provider;

import com.github.authzsql.model.Permission;

import java.util.List;
import java.util.Set;

/**
 * Permissions provider interface.
 *
 * @author wsg
 */
public interface PermissionsProvider {

    /**
     * Get operations.
     *
     * @param resourceType resource type
     * @return operations
     */
    Set<String> operations(String resourceType);

    /**
     * Get permissions.
     *
     * @param resourceType resource type
     * @param operation operation type
     * @return permissions
     */
    List<Permission> permissions(String resourceType, String operation);
}
