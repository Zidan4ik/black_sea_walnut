package org.example.black_sea_walnut.enums;

public enum LanguageCode {
    uk, en;

    public static LanguageCode fromString(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Language code cannot be null or blank");
        }
        try {
            return LanguageCode.valueOf(code.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid language code: " + code);
        }
    }
}
