package com.example.deliveryapp.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
public class OrderResponseDto {
    private final Long orderId;
    private final String memberName;
    private final String storeName;
    private final String state;
    private final List<OrderDetailResponseDto> orderDetailResponseDtos;

    @Builder
    public OrderResponseDto(Long orderId, String memberName, String storeName, String state, List<OrderDetailResponseDto> orderDetailResponseDtos) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.storeName = storeName;
        this.state = state;
        this.orderDetailResponseDtos = orderDetailResponseDtos;
    }
}
