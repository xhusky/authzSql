package com.github.authzsql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 权限控制SQL工具类
 *
 * @author Think wong
 */
public class PermissionHelper {

    // 权限控制魔字串
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
    private ConditionsProvider<Condition> conditionsProvider;

    private PermissionHelper() {
        // constructor disabled
    }

    public static class Builder {
        private PermissionHelper permissionHelper = new PermissionHelper();

        public Builder sql(String sql) {
            permissionHelper.originalSql = sql;
            return this;
        }
        public Builder provider(ConditionsProvider<Condition> conditionsProvider) {
            permissionHelper.conditionsProvider = conditionsProvider;
            return this;
        }

        public PermissionHelper build() {
            assert permissionHelper.originalSql != null;
            assert permissionHelper.conditionsProvider != null;
            return permissionHelper;
        }
    }

    /**
     * 判断是否需要生成权限控制sql
     *
     * @return 是否需要生成权限控制sql
     */
    public static boolean needPermission(String sql) {
        return PATTERN_PLACEHOLDER.matcher(sql).find();
    }

    /**
     * 生成权限控制sql
     *
     * @return 权限控制sql
     */
    public String generatePermissionSql() {
        return infillCondition(extractSqlConditionMap());
    }

    /**
     * 向占位符填充条件，组成新的sql
     *
     * @return 填入条件的sql
     */
    private String infillCondition(Map<String, String> sqlConditionMap) {

        String destinationSql = originalSql;
        for (Map.Entry<String, String> entry : sqlConditionMap.entrySet()) {
            destinationSql = destinationSql.replaceAll(entry.getKey(), entry.getValue());
        }

        return destinationSql;
    }

    /**
     * 抽取筛选字段，组成where条件
     *
     * @return 组装好的where条件Map key为placeholder, value为实际where条件
     */
    private Map<String, String> extractSqlConditionMap() {
        final Matcher matcher = PATTERN_PLACEHOLDER_EXTRACTOR.matcher(originalSql);
        Map<String, String> sqlConditionMap = new HashMap<>();

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            sqlConditionMap.put(placeholder, extractSqlCondition(placeholder));
        }

        return sqlConditionMap;
    }

    /**
     * 从placeholder中抽取筛选字段，组成where条件
     *
     * @return 组装好的where条件
     */
    private String extractSqlCondition(String placeholder) {
        final Matcher matcher = PATTERN_PLACEHOLDER_COLUMN_EXTRACTOR.matcher(placeholder);
        if (matcher.find()) {
            String column = matcher.group(1);
            List<Condition> conditions = conditionsProvider.conditions(extractColumn(column));
            List<String> sqlConditions = generateSqlConditions(conditions, column);
            return generateSqlClause(sqlConditions);
        }

        return placeholder;
    }

    /**
     * 生成 Sql where 条件列表
     */
    private List<String> generateSqlConditions(List<Condition> conditions, String column) {
        List<String> sqlConditions = new ArrayList<>();

        for (Condition condition : conditions) {
            sqlConditions.add(condition.string(column));
        }

        return sqlConditions;
    }

    /**
     * 组装成where语句
     */
    private String generateSqlClause(List<String> sqlConditions) {
        return generateSqlClause(sqlConditions, OPEN, CLOSE, OR);
    }

    /**
     * 组装成where语句
     */
    private String generateSqlClause(List<String> sqlConditions, String open, String close, String conjunction) {

        StringBuilder builder = new StringBuilder();
        if (!sqlConditions.isEmpty()) {
            builder.append(open);
            for (int i = 0, n = sqlConditions.size(); i < n; i++) {
                String part = sqlConditions.get(i);
                if (i > 0) {
                    builder.append(conjunction);
                }
                builder.append(part);
            }
            builder.append(close);
        }

        return builder.toString();
    }

    /**
     * 获取真实列名称，去掉表别名， 比如a.column -> column
     */
    private String extractColumn(String column) {
        final Matcher matcher = PATTERN_COLUMN_NAME.matcher(column);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return column;
    }
}
