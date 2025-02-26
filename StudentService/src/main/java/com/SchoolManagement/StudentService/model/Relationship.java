package com.SchoolManagement.StudentService.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Relationship {
    MOTHER("Mother"),
    FATHER("Father"),
    GUARDIAN("Guardian");

    private final String value;

    Relationship(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Relationship fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return GUARDIAN; // Default value for empty or null input
        }
        for (Relationship relationship : Relationship.values()) {
            if (relationship.value.equalsIgnoreCase(value)) {
                return relationship;
            }
        }
        throw new IllegalArgumentException("Invalid relationship type: " + value);
    }
}

