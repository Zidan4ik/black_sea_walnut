package org.example.black_sea_walnut.enums;

public enum OrderStatus {
    inProgress,new_,close,cancel;
    public static OrderStatus fromString(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Order status cannot be null or blank");
        }
        try {
            return OrderStatus.valueOf(type.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + type);
        }
    }
}
