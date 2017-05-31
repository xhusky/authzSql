package com.github.authzsql;

import com.github.authzsql.model.SqlCondition;
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

    private static final String PLACEHOLDER_MAGIC = "IAmPlaceholder";
    private static final String PLACEHOLDER_ELEMENT = "'" + PLACEHOLDER_MAGIC + "-.*?\'";
    private static final String PLACEHOLDER_COLUMN_EXTRACTOR = "'" + PLACEHOLDER_MAGIC + "-(.*?)'\\s*=\\s*'" + PLACEHOLDER_MAGIC + "-.*?'";
    private static final String PLACEHOLDER = PLACEHOLDER_ELEMENT + "\\s*=\\s*" + PLACEHOLDER_ELEMENT;

    private static final Pattern PATTERN_PLACEHOLDER_COLUMN_EXTRACTOR = Pattern.compile(PLACEHOLDER_COLUMN_EXTRACTOR);
    private static final Pattern PATTERN_PLACEHOLDER = Pattern.compile(PLACEHOLDER);
    private static final Pattern PATTERN_PLACEHOLDER_EXTRACTOR = Pattern.compile("(" + PLACEHOLDER + ")");

    private static final Pattern PATTERN_COLUMN_NAME = Pattern.compile(".*\\.(.*)$");

    private static final String OR = " OR ";
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
    public static boolean needAuthz(String sql) {
        return PATTERN_PLACEHOLDER.matcher(sql).find();
    }

    /**
     * Generate authorization sql
     */
    public String generateAuthzSql() {
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
        final Matcher matcher = PATTERN_PLACEHOLDER_EXTRACTOR.matcher(originalSql);
        Map<String, String> sqlConditionMap = new HashMap<>();

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            sqlConditionMap.put(placeholder, extractConditionPlainSql(placeholder));
        }

        return sqlConditionMap;
    }

    /**
     * Extract sql condition from placeholder
     */
    private String extractConditionPlainSql(String placeholder) {
        final Matcher matcher = PATTERN_PLACEHOLDER_COLUMN_EXTRACTOR.matcher(placeholder);
        if (matcher.find()) {
            String column = matcher.group(1);
            List<SqlCondition> conditions = sqlConditionsProvider.conditions(extractColumn(column));
            List<String> sqlConditions = generateConditionPlainSqlList(conditions, column);
            return generateConditionSqlClause(sqlConditions);
        }

        return placeholder;
    }

    /**
     * Generate sql conditions
     */
    private List<String> generateConditionPlainSqlList(List<SqlCondition> conditions, String column) {
        List<String> sqlConditions = new ArrayList<>();

        for (SqlCondition sqlCondition : conditions) {
            sqlConditions.add(sqlCondition.string(column));
        }

        return sqlConditions;
    }

    /**
     * Generate condition sql clause
     */
    private String generateConditionSqlClause(List<String> sqlConditions) {
        return generateConditionSqlClause(sqlConditions, OPEN, CLOSE, OR);
    }

    /**
     * Generate condition sql clause
     */
    private String generateConditionSqlClause(List<String> sqlConditions, String open, String close, String conjunction) {

        StringBuilder builder = new StringBuilder();
        if (!sqlConditions.isEmpty()) {
            builder.append(open);
            for (int i = 0, n = sqlConditions.size(); i < n; i++) {
                String sqlCondition = sqlConditions.get(i);
                if (i > 0) {
                    builder.append(conjunction);
                }
                builder.append(sqlCondition);
            }
            builder.append(close);
        }

        return builder.toString();
    }

    /**
     * Extract column, remove table alias
     */
    private String extractColumn(String column) {
        final Matcher matcher = PATTERN_COLUMN_NAME.matcher(column);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return column;
    }
}
