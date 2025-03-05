package com.example.deliveryapp.order.dto.response;

import com.example.deliveryapp.order.entity.Order;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

@Getter
public class OrderInfoResponseDto {
    private final Long orderId;
    private final String orderNumber;
    private final String memberName;
    private final String storeName;
    private final String state;
    private final BigDecimal totalPrice;

    @Builder
    public OrderInfoResponseDto(Long orderId, String orderNumber, String memberName, String storeName, String state, BigDecimal totalPrice) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.memberName = memberName;
        this.storeName = storeName;
        this.state = state;
        this.totalPrice = totalPrice;
    }

    public static Page<OrderInfoResponseDto> toResponsePage(Page<Order> orders, Pageable pageable){
        return orders.map(order -> OrderInfoResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .memberName(order.getUser().getUserName())
                .storeName(order.getStore().getBusinessName())
                .state(order.getState().getDescription())
                .totalPrice(order.getTotalPrice())
                .build());
    }
}
