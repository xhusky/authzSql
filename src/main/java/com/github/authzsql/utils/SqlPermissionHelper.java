package com.github.authzsql.utils;

import com.github.authzsql.model.Constants;
import com.github.authzsql.model.LogicalOperator;
import com.github.authzsql.model.SqlCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class SqlPermissionHelper {

    /**
     * Generate condition sql clause
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
     * Generate condition sql clause
     */
    private static String generateConditionSqlClause(List<String> sqlConditions, String conjunction) {

        conjunction = StringWrapper.whitespace(conjunction);
        StringBuilder builder = new StringBuilder();
        if (!sqlConditions.isEmpty()) {
            builder.append(Constants.SQL_CONDITION_OPEN);
            for (int i = 0, n = sqlConditions.size(); i < n; i++) {
                String sqlCondition = sqlConditions.get(i);
                if (i > 0) {
                    builder.append(conjunction);
                }
                builder.append(sqlCondition);
            }
            builder.append(Constants.SQL_CONDITION_CLOSE);
        }

        return builder.toString();
    }

    public static String fillResourceType(String resourceType) {
        return Constants.PERMISSION_SYSTEM_PREFIX + resourceType;
    }
}