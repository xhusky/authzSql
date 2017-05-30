package com.github.authzsql;

/**
 * 条件实体
 *
 * @author Think wong
 */
public class Condition {
    private String column;
    private WhereOperator whereOperator;
    private String value;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public WhereOperator getWhereOperator() {
        return whereOperator;
    }

    public void setWhereOperator(WhereOperator whereOperator) {
        this.whereOperator = whereOperator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return column + whereOperator.condition(value);
    }

    public String string() {
        return toString();
    }

    public String string(String newColumn) {
        this.column = newColumn;
        return string();
    }
}
