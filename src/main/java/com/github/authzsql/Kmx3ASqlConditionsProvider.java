package com.github.authzsql;

import com.github.authzsql.model.ComparisonOperator;
import com.github.authzsql.model.SqlCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kmx conditions provider
 *
 * @author Think wong
 */
public class Kmx3ASqlConditionsProvider implements SqlConditionsProvider<SqlCondition> {

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
     * Extract list of condition by column name.
     *
     * @param column column name
     */
    @Override
    public List<SqlCondition> conditions(String column) {

        Matcher resourceMatcher = PATTERN_RESOURCE.matcher(PERMS);
        List<SqlCondition> sqlConditions = new ArrayList<>();

        while (resourceMatcher.find()) {
            SqlCondition sqlCondition = extractCondition(resourceMatcher.group(1));
            if (sqlCondition != null) {
                sqlConditions.add(sqlCondition);
            }
        }

        return sqlConditions;
    }

    /**
     * Extract condition from permission resource.
     *
     * @param resource permission resource
     */
    private static SqlCondition extractCondition(String resource) {
        final Matcher matcher = PATTERN_RESOURCE_TYPE.matcher(resource);
        if (matcher.find()) {
            return extractCondition(matcher.group(1), matcher.group(2));
        }
        return null;
    }

    /**
     * Extract condition.
     *
     * @param originalColumn original column name
     * @param originalValue  original column value
     */
    private static SqlCondition extractCondition(String originalColumn, String originalValue) {
        final Matcher matcher = PATTERN_OPERATOR_VALUE.matcher(originalValue);

        SqlCondition sqlCondition = new SqlCondition();
        sqlCondition.setColumn(extractColumn(originalColumn));

        if (matcher.find()) {
            sqlCondition.setOperator(ComparisonOperator.fromString(matcher.group(1)));
            sqlCondition.setValue(matcher.group(2));
            return sqlCondition;
        }
        sqlCondition.setOperator(ComparisonOperator.EQUAL);
        sqlCondition.setValue(originalValue);
        return sqlCondition;
    }

    /**
     * Extract column name.
     *
     * @param originalColumn original column name
     */
    private static String extractColumn(String originalColumn) {
        final Matcher matcher = PATTERN_COLUMN.matcher(originalColumn);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return originalColumn;
    }

}
