package com.github.authzsql;

import com.github.authzsql.utils.Preconditions;

/**
 * This guy is lazy, nothing left.
 *
 * @author Think Wong
 */
public class Kmx3APermissionProvider {

    private String url;
    private String username;
    private String resourceType;
    private String operation = "view";
    private String effect = "allow";

    private Kmx3APermissionProvider() {
        // constructor disable
    }

    public static class Builder {
        private Kmx3APermissionProvider kmx3APermissionProvider = new Kmx3APermissionProvider();

        public Builder url(String url) {
            Preconditions.checkEmptyString(url, "You must provide a kmx3a url");
            kmx3APermissionProvider.url = url;
            return this;
        }

        public Builder username(String username) {
            Preconditions.checkEmptyString(username, "You must provide an user");
            kmx3APermissionProvider.username = username;
            return this;
        }

        public Builder resourceType(String resourceType) {
            Preconditions.checkEmptyString(resourceType, "You must provide a resource type");
            kmx3APermissionProvider.resourceType = resourceType;
            return this;
        }

        public Builder operation(String operation) {
            Preconditions.checkEmptyString(operation, "You must provide an operation");
            kmx3APermissionProvider.operation = operation;
            return this;
        }

        public Builder effect(String effect) {
            Preconditions.checkEmptyString(effect, "You must provide an effect type");
            kmx3APermissionProvider.effect = effect;
            return this;
        }

        public Kmx3APermissionProvider build() {
            checkPreconditions();
            return kmx3APermissionProvider;
        }

        private void checkPreconditions() {
            Preconditions.checkEmptyString(kmx3APermissionProvider.url, "You must provide kmx3a url");
            Preconditions.checkEmptyString(kmx3APermissionProvider.username, "You must provide an user");
            Preconditions.checkEmptyString(kmx3APermissionProvider.resourceType, "You must provide a resource type");
        }
    }
}
