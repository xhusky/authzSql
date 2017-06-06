package com.github.authzsql.plugins;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public interface SqlPermissionPlugin {

    /**
     * transform sql
     *
     * @param originalSql original sql
     * @return transformed sql
     */
    String transformSql(String originalSql);

    /**
     * @param originalSql original sql
     * @return true if support to transform
     */
    boolean support(String originalSql);

}
