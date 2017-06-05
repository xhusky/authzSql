package com.github.authzsql;

import java.util.List;

/**
 * Conditions provider
 *
 * @author Think Wong
 */
public interface SqlConditionsProvider<T> {
    List<T> conditions(String resourceType, String operation);

    List<T> conditions(String resourceType, String operation, String column);
}
