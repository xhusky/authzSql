package com.github.authzsql.model;

import java.util.Arrays;

/**
 * Where operator for Mysql
 *
 * @author Think wong
 */
public enum MysqlComparisonOperator implements ComparisonOperator {

    EQUAL("=") {
        public String condition(String columnValue) {
            return " = " + quote(columnValue);
        }
    },
    IN("IN") {
        public String condition(String columnValue) {
            String[] values = columnValue.split(",");
            for (int i = 0; i < values.length; i++) {
                values[i] = quote(values[i]);
            }
            return " IN (" + String.join(",", Arrays.asList(values)) + ")";
        }
    },
    LIKE("LIKE") {
        public String condition(String columnValue) {
            return " LIKE " + quote(columnValue);
        }
    },
    NOT_EQUAL("<>") {
        public String condition(String columnValue) {
            return " <> " + quote(columnValue);
        }
    },
    NOT_IN("NOT IN") {
        public String condition(String columnValue) {
            String[] values = columnValue.split(",");
            for (int i = 0; i < values.length; i++) {
                values[i] = quote(values[i]);
            }
            return " NOT IN (" + String.join(",", Arrays.asList(values)) + ")";
        }
    },
    NOT_LIKE("NOT LIKE") {
        public String condition(String columnValue) {
            return " NOT LIKE " + quote(columnValue);
        }
    },
    ALL("") {
        public String condition(String columnValue) {
            return "";
        }
    };

    MysqlComparisonOperator(String symbol) {
        this.symbol = symbol;
    }

    private final String symbol;

    @Override
    public String toString() {
        return symbol;
    }

    public String symbol() {
        return symbol;
    }

    public static MysqlComparisonOperator fromString(String name) {
        return valueOf(name.toUpperCase());
    }

    private static String quote(String str) {
        return (str != null ? "'" + str + "'" : null);
    }

}
