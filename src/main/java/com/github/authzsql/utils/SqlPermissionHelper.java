package com.github.authzsql.utils;

import com.github.authzsql.model.Constants;
import com.github.authzsql.model.LogicalOperator;
import com.github.authzsql.model.Permission;
import com.github.authzsql.model.SqlCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This guy is lazy, nothing left.
 *
 * @author wsg
 */
public class SqlPermissionHelper {

    /**
     * Generate condition sql clause.
     */
    public static String generateConditionSqlClause(List<SqlCondition> sqlConditions) {

        if (sqlConditions == null || sqlConditions.isEmpty()) {
            return Constants.SQL_CONDITION_FALSE;
        }

        Map<LogicalOperator, List<String>> logicalOperatorListMap = new HashMap<>();

        for (SqlCondition sqlCondition : sqlConditions) {
            LogicalOperator logicalOperator = sqlCondition.getLogicalOperator();
            logicalOperatorListMap.putIfAbsent(logicalOperator, new ArrayList<>());
            logicalOperatorListMap.get(logicalOperator).add(sqlCondition.string());
        }

        StringBuilder sqlClause = new StringBuilder();

        int i = 0;
        for (Map.Entry<LogicalOperator, List<String>> entry : logicalOperatorListMap.entrySet()) {
            if (i > 0) {
                sqlClause.append(" AND ");
            }
            i++;
            sqlClause.append(generateConditionSqlClause(entry.getValue(), entry.getKey().name()));
        }
        return sqlClause.toString();
    }

    /**
     * Generate condition sql clause.
     */
    private static String generateConditionSqlClause(List<String> sqlConditions,
        String conjunction) {

        String wrappedConjunction = StringWrapper.whitespace(conjunction);
        StringBuilder builder = new StringBuilder();
        if (!sqlConditions.isEmpty()) {
            builder.append(Constants.SQL_CONDITION_OPEN);

            int i = 0;
            for (String sqlCondition : sqlConditions) {
                if (i > 0) {
                    builder.append(wrappedConjunction);
                }
                i++;
                builder.append(sqlCondition);
            }
            builder.append(Constants.SQL_CONDITION_CLOSE);
        }

        return builder.toString();
    }

    public static Map<String, Map<String, List<Permission>>> splitPermissions(
        List<Permission> permissions) {
        Map<String, Map<String, List<Permission>>> splitMap = new HashMap<>();
        for (Permission permission : permissions) {

            String resourceType = permission.getResourceType();
            String operation = permission.getOperation().toUpperCase();

            splitMap.putIfAbsent(resourceType, new HashMap<>());
            splitMap.get(resourceType).putIfAbsent(operation, new ArrayList<>());
            splitMap.get(resourceType).get(operation).add(permission);
        }

        return splitMap;
    }

    public static String fillResourceType(String resourceType) {
        return Constants.PERMISSION_SYSTEM_PREFIX + resourceType;
    }
}
