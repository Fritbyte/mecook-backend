package com.mecook.mecookbackend.domain.model;

public enum DishContentType {
    HEADER,
    TEXT,
    IMAGE;

    public static boolean isValid(String type) {
        for (DishContentType contentType : values()) {
            if (contentType.name().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}
