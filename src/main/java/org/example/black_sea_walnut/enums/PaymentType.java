package org.example.black_sea_walnut.enums;

public enum PaymentType {
    invoice, card, cash;

    public static PaymentType fromString(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Payment type cannot be null or blank");
        }
        try {
            return PaymentType.valueOf(type.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid payment type: " + type);
        }
    }
}
