package com.github.authzsql.model;

/**
 * SQl placeholder entity.
 *
 * @author Think Wong
 */
public class SqlPlaceholder {

    private String text;
    private String type;
    private String column;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

}
