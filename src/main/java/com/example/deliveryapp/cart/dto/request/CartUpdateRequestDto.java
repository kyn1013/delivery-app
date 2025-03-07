package com.example.deliveryapp.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartUpdateRequestDto {

    @NotNull(message = "수량은 필수 입력값입니다.")
    @Min(value = 1, message = "주문 수량은 1 이상이어야 합니다.")
    private Long quantity;

    @NotNull(message = "메뉴는 필수 입력값입니다.")
    private Long menuId;
}
