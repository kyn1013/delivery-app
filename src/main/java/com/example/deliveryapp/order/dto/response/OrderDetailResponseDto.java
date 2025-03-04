package com.example.deliveryapp.order.dto.response;

import com.example.deliveryapp.order.entity.OrderDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class OrderDetailResponseDto {
    private final Long orderId;
    private final Long menuId;
    private final String menuName;
    private final Long quantity;

    @Builder
    public OrderDetailResponseDto(Long orderId, Long menuId, String menuName, Long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
    }

    public static List<OrderDetailResponseDto> toResponse(List<OrderDetail> orderDetails){
        List<OrderDetailResponseDto> orderDetailResponseDtoList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails){
            OrderDetailResponseDto orderDetailResponseDto = OrderDetailResponseDto.builder()
                                                    .orderId(orderDetail.getOrder().getId())
                                                    .menuId(orderDetail.getPMenu().getId())
                                                    .menuName(orderDetail.getPMenu().getName())
                                                    .quantity(orderDetail.getQuantity())
                                                    .build();
            orderDetailResponseDtoList.add(orderDetailResponseDto);
        }
        return orderDetailResponseDtoList;
    }
}
