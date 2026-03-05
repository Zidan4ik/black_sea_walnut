package org.example.black_sea_walnut.enums;

public enum RegisterType {
    legal, fop, individual;

    public static RegisterType fromString(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        try {
            return RegisterType.valueOf(code.toLowerCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
