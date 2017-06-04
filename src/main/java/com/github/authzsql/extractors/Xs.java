package com.github.authzsql.extractors;

import com.github.authzsql.model.ComparisonOperator;
import com.github.authzsql.model.LogicalOperator;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class Xs {
    public static LogicalOperator operator(ComparisonOperator operator) {
        switch (operator) {
            case IN:
            case LIKE:
            case EQUAL:
            case ALL:
                return LogicalOperator.OR;
            case NOT_IN:
            case UNEQUAL:
            case NOT_LIKE:
                return LogicalOperator.AND
        }

    }
}
