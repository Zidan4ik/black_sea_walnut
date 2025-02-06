package org.example.black_sea_walnut.enums;

public enum RegisterType {
    legal,fop,individual;
    public static RegisterType fromString(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Type cannot be null or blank");
        }
        try {
            return RegisterType.valueOf(code.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid type: " + code);
        }
    }
}
