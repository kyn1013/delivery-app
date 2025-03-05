package com.example.deliveryapp.order.dto.response;

import com.example.deliveryapp.order.entity.OrderDetail;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
public class OrderDetailResponseDto {
    private final Long orderId;
    private final Long menuId;
    private final String menuName;
    private final Long quantity;
    private final BigDecimal price;

    @Builder
    public OrderDetailResponseDto(Long orderId, Long menuId, String menuName, Long quantity, BigDecimal price) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.price = price;
    }

    public static List<OrderDetailResponseDto> toResponse(List<OrderDetail> orderDetails){
        List<OrderDetailResponseDto> orderDetailResponseDtoList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails){
            OrderDetailResponseDto orderDetailResponseDto = OrderDetailResponseDto.builder()
                                                    .orderId(orderDetail.getOrder().getId())
                                                    .menuId(orderDetail.getMenu().getId())
                                                    .menuName(orderDetail.getMenu().getMenuName())
                                                    .quantity(orderDetail.getQuantity())
                                                    .price(orderDetail.getPrice())
                                                    .build();
            orderDetailResponseDtoList.add(orderDetailResponseDto);
        }
        return orderDetailResponseDtoList;
    }
}
