package com.example.deliveryapp.cart.dto.request;

import lombok.Getter;

@Getter
public class CartUpdateRequestDto {
    private Long quantity;
    private Long menuId;
}
