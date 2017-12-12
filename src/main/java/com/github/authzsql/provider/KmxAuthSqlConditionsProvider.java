package com.github.authzsql.provider;

import com.github.authzsql.model.ComparisonOperator;
import com.github.authzsql.model.Constants;
import com.github.authzsql.model.Permission;
import com.github.authzsql.model.SqlCondition;
import com.github.authzsql.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kmx auth conditions provider.
 *
 * @author wsg
 */
public class KmxAuthSqlConditionsProvider implements SqlConditionsProvider {

    /**
     * eg, LIKE:3102%, IN:1001,1002,1003.
     */
    private static final Pattern PATTERN_OPERATOR_VALUE = Pattern.compile("(.*?):(.*)$");

    private PermissionsProvider permissionsProvider;

    public KmxAuthSqlConditionsProvider(PermissionsProvider permissionsProvider) {
        Preconditions.checkNotNull(permissionsProvider, "permissionsProvider can't be null");
        this.permissionsProvider = permissionsProvider;
    }

    @Override
    public List<SqlCondition> conditions(String resourceType, String operation, String column) {
        List<Permission> permissions = permissionsProvider.permissions(resourceType, operation);
        List<SqlCondition> sqlConditions = new ArrayList<>();

        for (Permission permission : permissions) {
            sqlConditions.add(extractCondition(permission.getResourceInfo(), column));
        }
        return sqlConditions;
    }

    @Override
    public Set<String> operations(String resourceType) {
        return permissionsProvider.operations(resourceType);
    }

    /**
     * Extract condition.
     *
     * @param resourceInfo resource info
     * @param column column name
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
