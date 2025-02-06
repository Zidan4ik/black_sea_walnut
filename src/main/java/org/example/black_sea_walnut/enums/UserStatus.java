package org.example.black_sea_walnut.enums;

public enum UserStatus {
    isActive, isDeleted;
    public static UserStatus fromString(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Status cannot be null or blank");
        }
        try {
            return UserStatus.valueOf(code);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Status: " + code);
        }
    }
}
