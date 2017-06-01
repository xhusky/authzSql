package com.github.authzsql.model;

/**
 * Sql condition entity
 *
 * @author Think wong
 */
public class SqlCondition {
    private String column;
    private ComparisonOperator operator;
    private String value;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    public void setOperator(ComparisonOperator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return column + operator.condition(value);
    }

    public String string() {
        return toString();
    }

    public String string(String newColumn) {
        this.column = newColumn;
        return string();
    }
}