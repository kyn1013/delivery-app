package com.example.deliveryapp.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;


@Getter
public class OrderResponseDto {
    private final Long orderId;
    private final String orderNumber;
    private final String memberName;
    private final String storeName;
    private final String state;
    private final BigDecimal totalPrice;
    private final List<OrderDetailResponseDto> orderDetailResponseDtos;

    @Builder
    public OrderResponseDto(Long orderId, String orderNumber, String memberName, String storeName, String state, BigDecimal totalPrice, List<OrderDetailResponseDto> orderDetailResponseDtos) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.memberName = memberName;
        this.storeName = storeName;
        this.state = state;
        this.totalPrice = totalPrice;
        this.orderDetailResponseDtos = orderDetailResponseDtos;
    }
}
