package com.example.deliveryapp.cart.service;

import com.example.deliveryapp.cart.dto.request.CartSaveRequestDto;
import com.example.deliveryapp.cart.dto.request.CartUpdateRequestDto;
import com.example.deliveryapp.cart.dto.response.CartResponseDto;
import com.example.deliveryapp.cart.entity.Cart;
import com.example.deliveryapp.cart.repository.CartRepository;
import com.example.deliveryapp.order.practice_repository.MemberRepository;
import com.example.deliveryapp.order.practice_repository.PMenuRepository;
import com.example.deliveryapp.order.pratice_entity.Member;
import com.example.deliveryapp.order.pratice_entity.PMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.deliveryapp.cart.dto.response.CartResponseDto.toResponsePage;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final PMenuRepository pMenuRepository;

    @Transactional
    public CartResponseDto save(CartSaveRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new RuntimeException("데이터가 없습니다"));
        PMenu menu = pMenuRepository.findById(requestDto.getMenuId()).orElseThrow(() -> new RuntimeException("데이터가 없습니다"));
        Cart cart = new Cart(menu, member, requestDto.getQuantity());
        Cart savedCart = cartRepository.save(cart);
        Cart readCart = cartRepository.findByIdWithMenuAndMember(savedCart.getId());
        return CartResponseDto.builder()
                .cartId(readCart.getId())
                .menuId(readCart.getPMenu().getId())
                .menuName(readCart.getPMenu().getName())
                .memberId(readCart.getMember().getId())
                .memberName(readCart.getMember().getName())
                .quantity(readCart.getQuantity())
                .build();
    }

    public Page<CartResponseDto> getCarts(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Cart> cartPage = cartRepository.findByMemberId(pageable, userId);
        Page<CartResponseDto> responsePage = toResponsePage(cartPage, pageable);
        return responsePage;
    }

    @Transactional
    public CartResponseDto update(CartUpdateRequestDto updateRequestDto, Long cartId) {
        PMenu menu = pMenuRepository.findById(updateRequestDto.getMenuId()).orElseThrow(() -> new RuntimeException("데이터가 없습니다"));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("데이터가 없습니다"));
        cart.update(menu, updateRequestDto.getQuantity());
        Cart readCart = cartRepository.findByIdWithMenuAndMember(cart.getId());
        return CartResponseDto.builder()
                .cartId(readCart.getId())
                .menuId(readCart.getPMenu().getId())
                .menuName(readCart.getPMenu().getName())
                .memberId(readCart.getMember().getId())
                .memberName(readCart.getMember().getName())
                .quantity(readCart.getQuantity())
                .build();
    }

    public void delete(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("데이터가 없습니다"));
        cartRepository.delete(cart);
    }
}
