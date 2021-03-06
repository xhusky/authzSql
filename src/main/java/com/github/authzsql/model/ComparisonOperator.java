package com.github.authzsql.model;

import com.github.authzsql.utils.StringWrapper;

import java.util.Arrays;

/**
 * Comparison operator.
 *
 * @author wsg
 */
public enum ComparisonOperator {

    EQUAL {
        @Override
        public String condition(String column, String columnValue) {
            return column + " = " + StringWrapper.quote(columnValue);
        }
    },
    IN {
        @Override
        public String condition(String column, String columnValue) {
            String[] values = columnValue.split(",");
            for (int i = 0; i < values.length; i++) {
                values[i] = StringWrapper.quote(values[i]);
            }
            return column + " IN (" + String.join(",", Arrays.asList(values)) + ")";
        }
    },
    LIKE {
        @Override
        public String condition(String column, String columnValue) {
            return column + " LIKE " + StringWrapper.quote(columnValue);
        }
    },
    NOT_EQUAL {
        @Override
        public String condition(String column, String columnValue) {
            return column + " <> " + StringWrapper.quote(columnValue);
        }
    },
    NOT_IN {
        @Override
        public String condition(String column, String columnValue) {
            String[] values = columnValue.split(",");
            for (int i = 0; i < values.length; i++) {
                values[i] = StringWrapper.quote(values[i]);
            }
            return column + " NOT IN (" + String.join(",", Arrays.asList(values)) + ")";
        }
    },
    NOT_LIKE {
        @Override
        public String condition(String column, String columnValue) {
            return column + " NOT LIKE " + StringWrapper.quote(columnValue);
        }
    },
    ALL {
        @Override
        public String condition(String column, String columnValue) {
            return Constants.SQL_CONDITION_TRUE;
        }
    };

    public static ComparisonOperator fromString(String name) {
        return valueOf(name.toUpperCase());
    }

    public abstract String condition(String column, String value);
}
