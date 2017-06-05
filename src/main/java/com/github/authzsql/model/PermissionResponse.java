package com.github.authzsql.model;

import java.util.List;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class PermissionResponse {
    private Integer code;
    private String message;
    private List<Permission> permissions;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
