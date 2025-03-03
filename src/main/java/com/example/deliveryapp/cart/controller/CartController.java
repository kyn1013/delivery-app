package com.example.deliveryapp.cart.controller;

import com.example.deliveryapp.cart.dto.request.CartSaveRequestDto;
import com.example.deliveryapp.cart.dto.response.CartSaveResponseDto;
import com.example.deliveryapp.cart.service.CartService;
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
    @PostMapping
    public ResponseEntity<CartSaveResponseDto> save(@RequestBody CartSaveRequestDto requestDto){
        CartSaveResponseDto responseDto = cartService.save(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 장바구니 목록 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Page<CartSaveResponseDto>> getCarts(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @PathVariable Long userId
    ){
        Page<CartSaveResponseDto> responseDtos = cartService.getCarts(page, size, userId);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }


}
