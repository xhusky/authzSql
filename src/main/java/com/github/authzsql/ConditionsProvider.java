package com.github.authzsql;

import java.util.List;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public interface ConditionsProvider<T> {

    List<T> conditions(String column);

}
