package com.example.deliveryapp.cart.controller;

import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.cart.dto.request.CartSaveRequestDto;
import com.example.deliveryapp.cart.dto.request.CartUpdateRequestDto;
import com.example.deliveryapp.cart.dto.response.CartResponseDto;
import com.example.deliveryapp.cart.service.CartService;
import com.example.deliveryapp.common.annotation.Auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    /*
     * 장바구니 추가
     */
    @PostMapping()
    public ResponseEntity<CartResponseDto> save(@Auth AuthUser authUser, @Valid @RequestBody CartSaveRequestDto requestDto){
        CartResponseDto responseDto = cartService.save(authUser, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 장바구니 목록 조회
     */
    @GetMapping()
    public ResponseEntity<Page<CartResponseDto>> getCarts(@Auth AuthUser authUser,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size
    ){
        Page<CartResponseDto> responseDtos = cartService.getCarts(page, size, authUser);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    /*
     * 장바구니 수정
     */
    @PatchMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> update(@Auth AuthUser authUser, @Valid @RequestBody CartUpdateRequestDto updateRequestDto, @PathVariable Long cartId) {
         CartResponseDto responseDto = cartService.update(authUser, updateRequestDto, cartId);
         return new ResponseEntity<>(responseDto, HttpStatus.OK);
     }

    /*
     * 장바구니 삭제
     */
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> delete(@Auth AuthUser authUser, @PathVariable Long cartId){
        cartService.delete(authUser, cartId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
