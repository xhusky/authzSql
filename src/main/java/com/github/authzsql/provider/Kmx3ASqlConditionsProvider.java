package com.github.authzsql.provider;

import com.github.authzsql.cache.PermissionCache;
import com.github.authzsql.model.ComparisonOperator;
import com.github.authzsql.model.Constants;
import com.github.authzsql.model.Permission;
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

    // eg, LIKE:3102%
    private static final Pattern PATTERN_OPERATOR_VALUE = Pattern.compile("(.*?):(.*)$");

    /**
     * Extract list of condition by resource type and operation type.
     *
     * @param resourceType resource type
     * @param operation    operation type
     * @param column       column name
     */
    @Override
    public List<SqlCondition> conditions(String resourceType, String operation, String column) {
        List<Permission> permissions = PermissionCache.permissions(resourceType, operation);
        List<SqlCondition> sqlConditions = new ArrayList<>();

        for (Permission permission : permissions) {
            sqlConditions.add(extractCondition(permission.getResourceInfo(), column));
        }
        return sqlConditions;
    }

    /**
     * Extract condition.
     *
     * @param resourceInfo resource info
     * @param column       column name
     */
    private SqlCondition extractCondition(String resourceInfo, String column) {
        final Matcher matcher = PATTERN_OPERATOR_VALUE.matcher(resourceInfo);

        SqlCondition sqlCondition = new SqlCondition();
        sqlCondition.setColumn(column);

        if (matcher.find()) {
            sqlCondition.setOperator(ComparisonOperator.fromString(matcher.group(1)));
            sqlCondition.setValue(matcher.group(2));
            return sqlCondition;
        }
        if (Constants.PERMISSION_ALL_VALUE.equals(resourceInfo)) {
            sqlCondition.setOperator(ComparisonOperator.ALL);
            sqlCondition.setValue(resourceInfo);
        } else {
            sqlCondition.setOperator(ComparisonOperator.EQUAL);
            sqlCondition.setValue(resourceInfo);
        }
        return sqlCondition;
    }

}
