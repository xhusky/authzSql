package com.github.authzsql.plugins;

import com.github.authzsql.model.Constants;
import com.github.authzsql.model.SqlCondition;
import com.github.authzsql.provider.SqlConditionsProvider;
import com.github.authzsql.utils.SqlPermissionHelper;
import com.github.authzsql.utils.StringWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sql operation permission plugin.
 *
 * @author Think Wong
 */
public class SqlOperationPermissionPlugin implements SqlPermissionPlugin {

    private static final Pattern PATTERN_OPERATION_PLACEHOLDER = Pattern.compile("'K2AUTH/(.*?)/(.*?)/(.*?)'");

    private String originalSql;
    private SqlConditionsProvider sqlConditionsProvider;

    public SqlOperationPermissionPlugin(SqlConditionsProvider sqlConditionsProvider) {
        this.sqlConditionsProvider = sqlConditionsProvider;
    }

    @Override
    public boolean support(String originalSql) {
        return PATTERN_OPERATION_PLACEHOLDER.matcher(originalSql).find();
    }

    @Override
    public String transformSql(String originalSql) {
        this.originalSql = originalSql;
        return infillConditionPlainSql(extractConditionPlainSqlMap());
    }

    /**
     * Extract sql condition map. key is placeholder, value is where condition
     */
    private Map<String, String> extractConditionPlainSqlMap() {
        final Matcher matcher = PATTERN_OPERATION_PLACEHOLDER.matcher(originalSql);
        Map<String, String> sqlConditionMap = new HashMap<>();

        while (matcher.find()) {
            String placeholder = matcher.group(0);
            sqlConditionMap.put(placeholder, extractConditionPlainSql(placeholder));
        }

        return sqlConditionMap;
    }

    /**
     * Infill condition
     */
    private String infillConditionPlainSql(Map<String, String> conditionPlainSqlMap) {

        String destinationSql = originalSql;
        for (Map.Entry<String, String> entry : conditionPlainSqlMap.entrySet()) {
            destinationSql = destinationSql.replaceAll(entry.getKey(), entry.getValue());
        }

        return destinationSql;
    }

    /**
     * Extract sql condition from placeholder
     */
    private String extractConditionPlainSql(String placeholder) {
        final Matcher matcher = PATTERN_OPERATION_PLACEHOLDER.matcher(placeholder);
        if (matcher.find()) {
            String resourceType = matcher.group(1);
            String operationType = matcher.group(2);
            String column = matcher.group(3);
            return extractConditionPlainSql(resourceType, operationType, column);
        }
        return placeholder;
    }

    private String extractConditionPlainSql(String resourceType, String operationType, String column) {
        StringBuilder sqlClause = new StringBuilder();
        Set<String> operationSet = new HashSet<>();

        if ("ALL".equals(operationType)) {
            operationSet = sqlConditionsProvider.operations(resourceType);
        } else {
            operationSet.clear();
            operationSet.add(operationType);
        }
        int i = 0;
        for (String operation : operationSet) {

            List<SqlCondition> sqlConditions = sqlConditionsProvider.conditions(resourceType, operation, column);
            if (i > 0) {
                sqlClause.append(",");
                sqlClause.append("\n");
            }
            i++;

            sqlClause.append(Constants.SQL_CONDITION_OPEN);
            sqlClause.append(Constants.SQL_OPERATION_SELECT);
            sqlClause.append(SqlPermissionHelper.generateConditionSqlClause(sqlConditions));
            sqlClause.append(Constants.SQL_CONDITION_CLOSE);

            String columnAlias = resourceType + "_" + operation;
            columnAlias = Constants.SQL_COLUMN_AS + StringWrapper.backQuote(columnAlias.toLowerCase());
            sqlClause.append(columnAlias);
        }

        return sqlClause.toString();
    }
}
