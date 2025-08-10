package com.jc.template.common.enums;

public enum RoleEnum {
    ROLE_ADMIN("1"),
    ROLE_USER("2")

    ;

    private final String roleId;

    RoleEnum(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }
}
