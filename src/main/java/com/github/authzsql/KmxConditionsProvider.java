package com.github.authzsql;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 调用权限 REST API 权限条件
 *
 * @author Think wong
 */
public class KmxConditionsProvider implements ConditionsProvider<Condition> {

    private static final Pattern PATTERN_RESOURCE = Pattern.compile("\"resource\"\\s*:\\s*\"(.*?)\"");
    private static final Pattern PATTERN_RESOURCE_TYPE = Pattern.compile("(.*)/(.*)$");
    private static final Pattern PATTERN_COLUMN = Pattern.compile(".*/(.*)$");
    private static final Pattern PATTERN_OPERATOR_VALUE = Pattern.compile("(.*?):(.*)$");

    private static final String PERMS = "{\n" +
            "    \"code\":0,\n" +
            "    \"message\":\"\",\n" +
            "    \"body\":{\n" +
            "        \"user\":{\n" +
            "            \"id\":\"1\",\n" +
            "            \"name\":\"abc\",\n" +
            "            \"permissions\":[\n" +
            "                {\n" +
            "                    \"resource\":\"gw-md4x/windfarm/LIKE:20014%\",\n" +
            "                    \"operation\":\"VIEW\",\n" +
            "                    \"effect\":\"allow\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"resource\":\"gw-md4x/windfarm/IN:200141,200142,200143\",\n" +
            "                    \"operation\":\"VIEW\",\n" +
            "                    \"effect\":\"allow\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"resource\":\"gw-md4x/windfarm/200146\",\n" +
            "                    \"operation\":\"VIEW\",\n" +
            "                    \"effect\":\"allow\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    }\n" +
            "}";



    /**
     * 根据列名获取权限数据
     *
     * @param column 列名
     * @return 权限对象列表
     */
    @Override
    public List<Condition> conditions(String column) {

        Matcher resourceMatcher = PATTERN_RESOURCE.matcher(PERMS);
        List<Condition> conditions = new ArrayList<>();

        while (resourceMatcher.find()) {
            Condition condition = extractCondition(resourceMatcher.group(1));
            if (condition != null) {
                conditions.add(condition);
            }
        }

        return conditions;
    }

    /**
     * 抽取单个资源条件
     *
     * @param resource 资源类型/资源 eg: gw-md4x/windfarm/IN:200141,200142,200143。gw-md4x/windfarm为资源类型
     *                 IN:200141,200142,200143为资源
     */
    private static Condition extractCondition(String resource) {
        final Matcher matcher = PATTERN_RESOURCE_TYPE.matcher(resource);
        if (matcher.find()) {
            return extractCondition(matcher.group(1), matcher.group(2));
        }
        return null;
    }

    /**
     * 抽取单个资源条件
     *
     * @param originalColumn 原始列名（可能包含系统名，比如gw-md4x/windfarm，其中gw-md4x为系统名称，windfarm为列名）
     * @param originalValue  原始资源 比如 IN:200141,200142,200143 IN为操作类型 200141,200142,200143为具体资源
     */
    private static Condition extractCondition(String originalColumn, String originalValue) {
        final Matcher matcher = PATTERN_OPERATOR_VALUE.matcher(originalValue);

        Condition condition = new Condition();
        condition.setColumn(extractColumn(originalColumn));

        if (matcher.find()) {
            condition.setWhereOperator(MysqlWhereOperator.valueOf(matcher.group(1)));
            condition.setValue(matcher.group(2));
            return condition;
        }
        condition.setWhereOperator(MysqlWhereOperator.EQUAL);
        condition.setValue(originalValue);
        return condition;
    }

    /**
     * 抽取列名
     *
     * @param originalColumn 原始列名（可能包含系统名，比如gw-md4x/windfarm，其中gw-md4x为系统名称，windfarm为列名）
     */
    private static String extractColumn(String originalColumn) {
        final Matcher matcher = PATTERN_COLUMN.matcher(originalColumn);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return originalColumn;
    }


}
