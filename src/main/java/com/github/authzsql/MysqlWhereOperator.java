package com.github.authzsql;

import java.util.Arrays;

/**
 * 权限控制SQL where 匹配类型
 *
 * @author Think wong
 */
public enum MysqlWhereOperator implements WhereOperator {

    EQUAL("=") {
        public String condition(String fieldValue) {
            return " = " + quote(fieldValue);
        }
    },
    IN("IN") {
        public String condition(String fieldValue) {
            String[] values = fieldValue.split(",");
            for (int i = 0; i < values.length; i++) {
                values[i] = quote(values[i]);
            }
            return " IN (" + String.join(",", Arrays.asList(values)) + ")";
        }
    },
    LIKE("LIKE") {
        public String condition(String fieldValue) {
            return " LIKE " + quote(fieldValue);
        }
    },
    NOT_EQUAL("<>") {
        public String condition(String fieldValue) {
            return " <> " + quote(fieldValue);
        }
    },
    NOT_IN("NOT IN") {
        public String condition(String fieldValue) {
            String[] values = fieldValue.split(",");
            for (int i = 0; i < values.length; i++) {
                values[i] = quote(values[i]);
            }
            return " NOT IN (" + String.join(",", Arrays.asList(values)) + ")";
        }
    },
    NOT_LIKE("NOT LIKE") {
        public String condition(String fieldValue) {
            return " NOT LIKE " + quote(fieldValue);
        }
    },
    ALL("") {
        public String condition(String fieldValue) {
            return "";
        }
    };

    MysqlWhereOperator(String symbol) {
        this.symbol = symbol;
    }

    private final String symbol;

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public MysqlWhereOperator fromString(String name) {
        return MysqlWhereOperator.valueOf(name.toUpperCase());
    }

    public String symbol() {
        return symbol;
    }

    private static String quote(String str) {
        return (str != null ? "'" + str + "'" : null);
    }

}
