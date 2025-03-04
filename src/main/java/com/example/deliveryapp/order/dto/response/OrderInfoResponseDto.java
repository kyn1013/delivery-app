package com.example.deliveryapp.order.dto.response;

import com.example.deliveryapp.order.entity.Order;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
public class OrderInfoResponseDto {
    private final Long orderId;
    private final String memberName;
    private final String storeName;
    private final String state;

    @Builder
    public OrderInfoResponseDto(Long orderId, String memberName, String storeName, String state) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.storeName = storeName;
        this.state = state;
    }

    public static Page<OrderInfoResponseDto> toResponsePage(Page<Order> orders, Pageable pageable){
        return orders.map(order -> OrderInfoResponseDto.builder()
                .orderId(order.getId())
                .memberName(order.getMember().getName())
                .storeName(order.getStore().getName())
                .state(order.getState())
                .build());
    }
}
