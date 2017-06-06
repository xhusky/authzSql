package com.github.authzsql;

import com.github.authzsql.plugins.SqlConditionPermissionPlugin;
import com.github.authzsql.plugins.SqlOperationPermissionPlugin;
import com.github.authzsql.plugins.SqlPermissionPlugin;
import com.github.authzsql.provider.Kmx3ASqlConditionsProvider;
import com.github.authzsql.provider.PermissionsProvider;
import com.github.authzsql.provider.SqlConditionsProvider;
import com.github.authzsql.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class SqlPermissionService {

    private String originalSql;
    private SqlConditionsProvider sqlConditionsProvider;
    private PermissionsProvider permissionsProvider;
    private List<SqlPermissionPlugin> sqlPermissionPlugins;

    private SqlPermissionService() {
        // constructor disabled
    }

    public static class Builder {
        private SqlPermissionService service = new SqlPermissionService();

        public Builder sql(String sql) {
            Preconditions.checkEmptyString(sql, "You must provide an original sql");
            service.originalSql = sql;
            return this;
        }

        public Builder sqlConditionsProvider(SqlConditionsProvider sqlConditionsProvider) {
            Preconditions.checkNotNull(sqlConditionsProvider, "sqlConditionsProvider can't be null");
            service.sqlConditionsProvider = sqlConditionsProvider;
            return this;
        }

        public Builder permissionsProvider(PermissionsProvider permissionsProvider) {
            Preconditions.checkNotNull(permissionsProvider, "permissionsProvider can't be null");
            service.permissionsProvider = permissionsProvider;
            return this;
        }

        public Builder sqlPermissionPlugins(List<SqlPermissionPlugin> sqlPermissionPlugins) {
            Preconditions.checkNotNull(sqlPermissionPlugins, "You must provide plugins");
            service.sqlPermissionPlugins = sqlPermissionPlugins;
            return this;
        }

        private void initConfig() {
            if(service.sqlConditionsProvider == null) {
                service.sqlConditionsProvider = new Kmx3ASqlConditionsProvider(service.permissionsProvider);
            }
            if (service.sqlPermissionPlugins == null || service.sqlPermissionPlugins.isEmpty()) {
                service.sqlPermissionPlugins = new ArrayList<>();
                service.sqlPermissionPlugins.add(new SqlConditionPermissionPlugin(service.sqlConditionsProvider));
                service.sqlPermissionPlugins.add(new SqlOperationPermissionPlugin(service.sqlConditionsProvider));
            }
        }

        private void checkPreconditions() {
            Preconditions.checkEmptyString(service.originalSql, "You must provide an original sql");
            Preconditions.checkNotNull(service.permissionsProvider, "You must provide a permissionsProvider");
        }

        public SqlPermissionService build() {
            checkPreconditions();
            initConfig();
            return service;
        }
    }

    public String transformSql() {
        String sql = originalSql;
        for (SqlPermissionPlugin sqlPermissionPlugin : sqlPermissionPlugins) {
            if (sqlPermissionPlugin.support(sql)) {
                sql = sqlPermissionPlugin.transformSql(sql);
            }
        }

        return sql;
    }

    public boolean support() {
        for (SqlPermissionPlugin sqlPermissionPlugin : sqlPermissionPlugins) {
            if (sqlPermissionPlugin.support(originalSql)) {
                return true;
            }
        }
        return false;
    }

}
