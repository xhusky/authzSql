package com.github.authzsql.plugins;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public interface SqlPermissionPlugin {
    String transformSql(String originalSql);
    boolean support(String originalSql);
}
