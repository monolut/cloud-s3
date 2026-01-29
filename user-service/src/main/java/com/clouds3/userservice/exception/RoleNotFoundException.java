package com.clouds3.userservice.exception;

import com.clouds3.userservice.enums.Role;

public class RoleNotFoundException extends RuntimeException {
    private RoleNotFoundException(String message) {
        super(message);
    }

    public static RoleNotFoundException byRole(Role role) {
        return new RoleNotFoundException(String.format("Role %s not found", role));
    }
}
