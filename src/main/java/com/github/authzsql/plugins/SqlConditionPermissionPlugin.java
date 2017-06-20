package com.github.authzsql.plugins;

import com.github.authzsql.model.SqlCondition;
import com.github.authzsql.provider.SqlConditionsProvider;
import com.github.authzsql.utils.SqlPermissionHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sql condition permission plugin.
 *
 * @author Think wong
 */
public class SqlConditionPermissionPlugin implements SqlPermissionPlugin {

    private static final Pattern PATTERN_CONDITION_PLACEHOLDER = Pattern.compile(
            "'K2AUTH/(.*?)/(.*?)/(.*?)'\\s*=\\s*0");

    private String originalSql;
    private SqlConditionsProvider sqlConditionsProvider;

    public SqlConditionPermissionPlugin(SqlConditionsProvider sqlConditionsProvider) {
        this.sqlConditionsProvider = sqlConditionsProvider;
    }

    @Override
    public boolean support(String originalSql) {
        return PATTERN_CONDITION_PLACEHOLDER.matcher(originalSql).find();
    }

    @Override
    public String transformSql(String originalSql) {
        this.originalSql = originalSql;
        return infillConditionPlainSql(extractConditionPlainSqlMap());
    }

    /**
     * Infill condition.
     */
    private String infillConditionPlainSql(Map<String, String> conditionPlainSqlMap) {

        String destinationSql = originalSql;
        for (Map.Entry<String, String> entry : conditionPlainSqlMap.entrySet()) {
            destinationSql = destinationSql.replaceAll(entry.getKey(), entry.getValue());
        }

        return destinationSql;
    }

    /**
     * Extract sql condition map. key is placeholder, value is where condition.
     */
    private Map<String, String> extractConditionPlainSqlMap() {
        final Matcher matcher = PATTERN_CONDITION_PLACEHOLDER.matcher(originalSql);
        Map<String, String> sqlConditionMap = new HashMap<>();

        while (matcher.find()) {
            String placeholder = matcher.group(0);
            sqlConditionMap.put(placeholder, extractConditionPlainSql(placeholder));
        }

        return sqlConditionMap;
    }

    /**
     * Extract sql condition from placeholder.
     */
    private String extractConditionPlainSql(String placeholder) {
        final Matcher matcher = PATTERN_CONDITION_PLACEHOLDER.matcher(placeholder);
        if (matcher.find()) {
            String resourceType = matcher.group(1);
            String operationType = matcher.group(2);
            String column = matcher.group(3);
            List<SqlCondition> conditions = sqlConditionsProvider.conditions(
                    resourceType, operationType, column);

            return SqlPermissionHelper.generateConditionSqlClause(conditions);
        }

        return placeholder;
    }
}
