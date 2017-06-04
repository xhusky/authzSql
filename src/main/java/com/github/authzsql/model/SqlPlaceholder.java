package com.github.authzsql.model;

/**
 * SQl placeholder entity.
 *
 * @author Think Wong
 */
public class SqlPlaceholder {

    private String text;
    private String resourceType;
    private String column;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
