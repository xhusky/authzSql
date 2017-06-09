package com.github.authzsql.model.converter;

import com.github.authzsql.model.ComparisonOperator;
import com.github.authzsql.model.LogicalOperator;

/**
 * Operator converter.
 *
 * @author Think Wong
 */
public class OperatorConverter {
    public static LogicalOperator comparisonToLogical(ComparisonOperator operator) {
        switch (operator) {
            case IN:
            case LIKE:
            case EQUAL:
                return LogicalOperator.OR;

            case NOT_IN:
            case NOT_EQUAL:
            case NOT_LIKE:
                return LogicalOperator.AND;

            case ALL:
                return LogicalOperator.OR;

            default:
                return LogicalOperator.OR;
        }
    }
}
