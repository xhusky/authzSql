package com.github.authzsql.provider;

import com.github.authzsql.model.SqlCondition;

import java.util.List;
import java.util.Set;

/**
 * Conditions provider.
 *
 * @author wsg
 */
public interface SqlConditionsProvider {

    /**
     * Get sqlConditions.
     *
     * @param resourceType resource type
     * @param operation operation type
     * @param column column name with table alias if exists
     * @return conditions
     */
    List<SqlCondition> conditions(String resourceType, String operation, String column);

    /**
     * Get operations.
     *
     * @param resourceType resource type
     * @return operations
     */
    Set<String> operations(String resourceType);
}
