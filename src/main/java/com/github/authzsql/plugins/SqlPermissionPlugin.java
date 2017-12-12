package com.github.authzsql.plugins;

/**
 * Sql permission plugin interface.
 *
 * @author wsg
 */
public interface SqlPermissionPlugin {

    /**
     * transform sql.
     *
     * @param originalSql original sql
     * @return transformed sql
     */
    String transformSql(String originalSql);

    /**
     * support transform or not.
     *
     * @param originalSql original sql
     * @return true if support to transform
     */
    boolean support(String originalSql);

}
