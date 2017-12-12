package com.github.authzsql.model.converter;

import com.github.authzsql.model.ComparisonOperator;
import com.github.authzsql.model.LogicalOperator;

/**
 * Operator converter.
 *
 * @author wsg
 */
public class OperatorConverter {

    /**
     * Comparison operator to logical operator.
     *
     * @param operator Comparison operator
     * @return Logical operator
     */
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
