package com.github.authzsql.provider;

import com.github.authzsql.model.Permission;

import java.util.List;
import java.util.Set;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public interface PermissionsProvider {
    /**
     * Get operations
     *
     * @param resourceType resource type
     */
    Set<String> operations(String resourceType);

    /**
     * Get permissions
     *
     * @param resourceType resource type
     * @param operation    operation type
     */
    List<Permission> permissions(String resourceType, String operation);
}
