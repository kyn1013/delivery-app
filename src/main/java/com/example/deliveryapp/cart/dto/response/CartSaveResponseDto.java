package com.example.deliveryapp.cart.dto.response;

import com.example.deliveryapp.cart.entity.Cart;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartSaveResponseDto {
    private final Long menuId;
    private final String menuName;
    private final Long memberId;
    private final String memberName;

    @Builder
    public CartSaveResponseDto(Long menuId, String menuName, Long memberId, String memberName) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.memberId = memberId;
        this.memberName = memberName;
    }

    public static Page<CartSaveResponseDto> toResponsePage(Page<Cart> carts, Pageable pageable) {
        return carts.map(cart -> CartSaveResponseDto.builder()
                .menuId(cart.getPMenu().getId())
                .menuName(cart.getPMenu().getName())
                .memberId(cart.getMember().getId())
                .memberName(cart.getMember().getName())
                .build()
        );
    }


}
