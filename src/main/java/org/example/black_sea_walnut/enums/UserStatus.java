package org.example.black_sea_walnut.enums;

public enum UserStatus {
    isActive, isDeleted;

    public static UserStatus fromString(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        try {
            return UserStatus.valueOf(code);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
