package com.github.authzsql;

import com.github.authzsql.model.Constants;
import com.github.authzsql.model.LogicalOperator;
import com.github.authzsql.model.OperationType;
import com.github.authzsql.model.SqlCondition;
import com.github.authzsql.model.SqlConditionPreparation;
import com.github.authzsql.model.converter.OperatorConverter;
import com.github.authzsql.utils.Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper for permission
 *
 * @author Think wong
 */
public class SqlPermissionHelper {

    // eg, 'K2AUTH/windfarm/a.windFarmId'<>''
    private static final Pattern PATTERN_CONDITION_PLACEHOLDER = Pattern.compile("'K2AUTH/(.*?)/(.*?)'\\s*<>\\s*'\\s*'");
    private static final String OPEN = " ( ";
    private static final String CLOSE = " ) ";

    private String originalSql;
    private SqlConditionsProvider<SqlCondition> sqlConditionsProvider = new Kmx3ASqlConditionsProvider();

    private SqlPermissionHelper() {
        // constructor disabled
    }

    public static class Builder {
        private SqlPermissionHelper sqlPermissionHelper = new SqlPermissionHelper();

        public Builder sql(String sql) {
            Preconditions.checkEmptyString(sql, "You must provide an original sql");
            sqlPermissionHelper.originalSql = sql;
            return this;
        }

        public Builder provider(SqlConditionsProvider<SqlCondition> sqlConditionsProvider) {
            Preconditions.checkNotNull(sqlConditionsProvider, "sqlConditionsProvider can't be null");
            sqlPermissionHelper.sqlConditionsProvider = sqlConditionsProvider;
            return this;
        }

        public SqlPermissionHelper build() {
            checkPreconditions();
            return sqlPermissionHelper;
        }

        private void checkPreconditions() {
            Preconditions.checkEmptyString(sqlPermissionHelper.originalSql, "You must provide an original sql");
        }
    }

    /**
     * Check whether need authorization
     *
     * @return true if the sql contains permission placeholder
     */
    public static boolean needAuthorization(String sql) {
        return PATTERN_CONDITION_PLACEHOLDER.matcher(sql).find();
    }

    /**
     * Generate authorization sql
     */
    public String generateAuthorizationSql() {
        return infillConditionPlainSql(extractConditionPlainSqlMap());
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
     * Extract sql condition map. key is placeholder, value is where condition
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
     * Extract sql condition from placeholder
     */
    private String extractConditionPlainSql(String placeholder) {
        final Matcher matcher = PATTERN_CONDITION_PLACEHOLDER.matcher(placeholder);
        if (matcher.find()) {
            String resourceType = matcher.group(1);
            String column = matcher.group(2);
            List<SqlCondition> conditions = sqlConditionsProvider.conditions(resourceType, OperationType.VIEW.name(), column);

            List<SqlConditionPreparation> sqlConditionPreparations = generateConditionPlainSqlList(conditions);
            return generateConditionSqlClause(sqlConditionPreparations);
        }

        return placeholder;
    }

    /**
     * Generate sql conditions
     */
    private List<SqlConditionPreparation> generateConditionPlainSqlList(List<SqlCondition> sqlConditions) {
        List<SqlConditionPreparation> sqlConditionPreparations = new ArrayList<>();

        for (SqlCondition sqlCondition : sqlConditions) {
            SqlConditionPreparation sqlConditionPreparation = new SqlConditionPreparation();
            sqlConditionPreparation.setSqlCondition(sqlCondition.string());
            sqlConditionPreparation.setLogicalOperator(OperatorConverter.ComparisonToLogical(sqlCondition.getOperator()));

            sqlConditionPreparations.add(sqlConditionPreparation);
        }

        return sqlConditionPreparations;
    }

    /**
     * Generate condition sql clause
     */
    private String generateConditionSqlClause(List<SqlConditionPreparation> sqlConditionPreparations) {

        if (sqlConditionPreparations == null || sqlConditionPreparations.isEmpty()) {
            return Constants.SQL_CONDITION_FALSE;
        }

        Map<LogicalOperator, List<String>> logicalOperatorListMap = new HashMap<>();

        for (SqlConditionPreparation sqlConditionPreparation : sqlConditionPreparations) {
            LogicalOperator logicalOperator = sqlConditionPreparation.getLogicalOperator();
            logicalOperatorListMap.putIfAbsent(logicalOperator, new ArrayList<>());
            logicalOperatorListMap.get(logicalOperator).add(sqlConditionPreparation.getSqlCondition());
        }

        StringBuilder sqlClause = new StringBuilder();

        int i = 0;
        for (Map.Entry<LogicalOperator, List<String>> entry : logicalOperatorListMap.entrySet()) {
            if (i > 0) {
                sqlClause.append("\n");
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
    private String generateConditionSqlClause(List<String> sqlConditions, String conjunction) {

        conjunction = wrap(conjunction);
        StringBuilder builder = new StringBuilder();
        if (!sqlConditions.isEmpty()) {
            builder.append(OPEN);
            for (int i = 0, n = sqlConditions.size(); i < n; i++) {
                String sqlCondition = sqlConditions.get(i);
                if (i > 0) {
                    builder.append(conjunction);
                }
                builder.append(sqlCondition);
            }
            builder.append(CLOSE);
        }

        return builder.toString();
    }

    private String wrap(String str) {
        return (str != null ? " " + str + " " : null);
    }
}
