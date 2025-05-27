package com.tasktrack.tasks.domain.oauth2.entity;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
