package com.example.deliveryapp.order.enums;

public enum OrderStatus {
    ORDER_REQUESTED(0, "주문요청"),
    ORDER_ACCEPTED(1, "주문수락"),
    DELETED(2, "삭제"),
    BLOCKED(3, "차단"),
    PENDING(4, "대기");

    private final int code;
    private final String description;

    OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
