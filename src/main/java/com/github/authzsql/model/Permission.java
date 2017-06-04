package com.github.authzsql.model;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class Permission {
    private String resourceType;
    private String resourceInfo;
    private String operation;
    private String effect;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceInfo() {
        return resourceInfo;
    }

    public void setResourceInfo(String resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "resourceType='" + resourceType + '\'' +
                ", resourceInfo='" + resourceInfo + '\'' +
                ", operation='" + operation + '\'' +
                ", effect='" + effect + '\'' +
                '}';
    }
}
