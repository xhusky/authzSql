package com.github.authzsql.utils;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class StringWrapper {
    public static String quote(String str) {
        return wrap(str, '\'');
    }

    public static String doubleQuote(String str) {
        return wrap(str, '"');
    }

    public static String backQuote(String str) {
        return wrap(str, '`');
    }


    public static String whitespace(String str) {
        return wrap(str, ' ');
    }

    public static String wrap(String str, char c) {
        return (str != null ? c + str + c : null);
    }

    public static String wrap(String str, String s) {
        return (str != null ? s + str + s : null);
    }
}
