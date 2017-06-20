package com.github.authzsql.provider;

import com.github.authzsql.model.SqlCondition;

import java.util.List;
import java.util.Set;

/**
 * Conditions provider.
 *
 * @author Think Wong
 */
public interface SqlConditionsProvider {

    /**
     * Get sqlConditions
     *
     * @param resourceType resource type
     * @param operation    operation type
     * @param column       column name with table alias if exists
     */
    List<SqlCondition> conditions(String resourceType, String operation, String column);

    /**
     * Get operations
     *
     * @param resourceType resource type
     */
    Set<String> operations(String resourceType);
}
