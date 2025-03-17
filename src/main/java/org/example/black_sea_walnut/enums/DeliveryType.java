package org.example.black_sea_walnut.enums;

public enum DeliveryType {
    courier,newMail,pickUp;
    public static DeliveryType fromString(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Delivery type cannot be null or blank");
        }
        try {
            return DeliveryType.valueOf(type.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid delivery type: " + type);
        }
    }
}
