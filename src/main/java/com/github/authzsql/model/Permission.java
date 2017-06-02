package com.github.authzsql.model;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class Permission {
    private String resourceType;
    private String operation;
    private String resource;
    private String effect;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
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
                ", operation='" + operation + '\'' +
                ", resource='" + resource + '\'' +
                ", effect='" + effect + '\'' +
                '}';
    }

}
