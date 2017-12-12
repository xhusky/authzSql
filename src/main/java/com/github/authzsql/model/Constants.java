package com.github.authzsql.model;

/**
 * Constants.
 *
 * @author wsg
 */
public interface Constants {

    String SQL_CONDITION_TRUE = "1 = 1";
    String SQL_CONDITION_FALSE = "1 = 0";
    String SQL_CONDITION_OPEN = "(";
    String SQL_CONDITION_CLOSE = ")";

    String SQL_OPERATION_SELECT = "SELECT COUNT(1) FROM DUAL WHERE ";

    String SQL_COLUMN_AS = " AS ";

    String PERMISSION_ALL_VALUE = "*";
    String SYSTEM_NAME = "md4x";
    String SYSTEM_SEPARATOR = "::";
    String PERMISSION_SYSTEM_PREFIX = SYSTEM_NAME + SYSTEM_SEPARATOR;

    String OPERATION_ALL = "ALL";
}



