package com.example.deliveryapp.cart.dto.response;

import com.example.deliveryapp.cart.entity.Cart;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
public class CartResponseDto {
    private final Long cartId;
    private final Long menuId;
    private final String menuName;
    private final Long memberId;
    private final String memberName;
    private final Long quantity;

    @Builder
    public CartResponseDto(Long cartId, Long menuId, String menuName, Long memberId, String memberName, Long quantity) {
        this.cartId = cartId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.memberId = memberId;
        this.memberName = memberName;
        this.quantity = quantity;
    }

    public static Page<CartResponseDto> toResponsePage(Page<Cart> carts, Pageable pageable) {
        return carts.map(cart -> CartResponseDto.builder()
                .cartId(cart.getId())
                .menuId(cart.getPMenu().getId())
                .menuName(cart.getPMenu().getName())
                .memberId(cart.getMember().getId())
                .memberName(cart.getMember().getName())
                .quantity(cart.getQuantity())
                .build()
        );
    }


}
