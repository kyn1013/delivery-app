package com.example.deliveryapp.order.enums;

import jakarta.persistence.AttributeConverter;

public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OrderStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer dbData) {
        return OrderStatus.of(dbData);
    }
}
