package com.github.authzsql;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public interface WhereOperator {
    String condition(String value);
    WhereOperator fromString(String name);
}
