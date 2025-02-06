package org.example.black_sea_walnut.enums;

public enum CallStatus {
    new_,close;
    public static CallStatus fromString(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Status cannot be null or blank");
        }
        try {
            return CallStatus.valueOf(code);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Status: " + code);
        }
    }
}
