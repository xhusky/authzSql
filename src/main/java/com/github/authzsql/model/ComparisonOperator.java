package com.github.authzsql.model;

import java.util.Arrays;

/**
 * Where operator for Mysql
 *
 * @author Think wong
 */
public enum ComparisonOperator {

    EQUAL {
        public String condition(String column, String columnValue) {
            return column + " = " + quote(columnValue);
        }
    },
    IN {
        public String condition(String column, String columnValue) {
            String[] values = columnValue.split(",");
            for (int i = 0; i < values.length; i++) {
                values[i] = quote(values[i]);
            }
            return column + " IN (" + String.join(",", Arrays.asList(values)) + ")";
        }
    },
    LIKE {
        public String condition(String column, String columnValue) {
            return column + " LIKE " + quote(columnValue);
        }
    },
    NOT_EQUAL {
        public String condition(String column, String columnValue) {
            return column + " <> " + quote(columnValue);
        }
    },
    NOT_IN {
        public String condition(String column, String columnValue) {
            String[] values = columnValue.split(",");
            for (int i = 0; i < values.length; i++) {
                values[i] = quote(values[i]);
            }
            return column + " NOT IN (" + String.join(",", Arrays.asList(values)) + ")";
        }
    },
    NOT_LIKE {
        public String condition(String column, String columnValue) {
            return column + " NOT LIKE " + quote(columnValue);
        }
    },
    ALL {
        public String condition(String column, String columnValue) {
            return Constants.SQL_CONDITION_TRUE;
        }
    };

    public static ComparisonOperator fromString(String name) {
        return valueOf(name.toUpperCase());
    }

    private static String quote(String str) {
        return (str != null ? "'" + str + "'" : null);
    }

    public abstract String condition(String column, String value);
}
