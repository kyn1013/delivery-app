package com.example.deliveryapp.cart.service;

import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.cart.dto.request.CartSaveRequestDto;
import com.example.deliveryapp.cart.dto.response.CartResponseDto;
import com.example.deliveryapp.cart.entity.Cart;
import com.example.deliveryapp.cart.repository.CartRepository;
import com.example.deliveryapp.common.exception.custom_exception.ServerException;
import com.example.deliveryapp.common.exception.errorcode.ErrorCode;
import com.example.deliveryapp.menu.entity.Menu;
import com.example.deliveryapp.menu.enums.MenuCategory;
import com.example.deliveryapp.menu.enums.MenuStatus;
import com.example.deliveryapp.menu.repository.MenuRepository;
import com.example.deliveryapp.store.entity.Store;
import com.example.deliveryapp.store.repository.StoreRepository;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.enums.UserRole;
import com.example.deliveryapp.user.repository.UserRepository;
import com.example.deliveryapp.user.service.UserService;
import org.apache.catalina.Manager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private StoreRepository storeRepository;

    @Mock
    private AuthUser authUser;

    @Test
    void 일반_회원이면_검증_메서드가_예외를_던지지_않는다() {
        // given
        AuthUser authUser = new AuthUser(1L, "customer@example.com", UserRole.ROLE_CUSTOMER);

        // when & then
        assertThatCode(() -> {
            // Reflection을 이용해서 private 메서드 호출
            java.lang.reflect.Method method = CartService.class.getDeclaredMethod("isValidCustomer", AuthUser.class);
            method.setAccessible(true);
            method.invoke(null, authUser); // static method는 객체 없이 호출
        }).doesNotThrowAnyException();
    }

    @Test
    void 일반_회원이_아니면_검증_메서드가_예외를_던진다() throws Exception {
        // given
        AuthUser authUser = new AuthUser(2L, "admin@example.com", UserRole.ROLE_OWNER);

        // when & then
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            Method method = CartService.class.getDeclaredMethod("isValidCustomer", AuthUser.class);
            method.setAccessible(true);
            method.invoke(null, authUser);
        });

        // 실제 예외를 가져와서 검증
        Throwable targetException = exception.getTargetException();
        assertInstanceOf(ServerException.class, targetException);
        assertEquals(ErrorCode.INVALID_MEMBER_ACCESS.getMessage(), targetException.getMessage());
    }

    @Test
    void save_일반_회원이_요청하면_장바구니_저장된다() {
        // given
        User user = createMockCustomer();
        Store store = createMockStore(user);
        Menu menu = new Menu("후라이드_치킨", BigDecimal.valueOf(15000), MenuCategory.MAIN, MenuStatus.AVAILABLE, 10, "바삭바삭한_후라이드_치킨", store);
        Cart savedCart = new Cart(menu, user, 2L);
        ReflectionTestUtils.setField(savedCart, "id", 1L);
        AuthUser authUser = new AuthUser(user.getId(), user.getEmail(), user.getUserRole());

        CartSaveRequestDto requestDto = new CartSaveRequestDto(1L, 2L);

        // Mocking
        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));
        when(storeRepository.findById(authUser.getId())).thenReturn(Optional.of(store));
        when(menuRepository.findById(requestDto.getMenuId())).thenReturn(Optional.of(menu));
        given(cartRepository.findByIdWithMenuAndMember(anyLong())).willReturn(savedCart);
        given(cartRepository.save(ArgumentMatchers.any(Cart.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        CartResponseDto result = cartService.save(authUser, requestDto);

        // then
        assertNotNull(result);
        assertEquals(menu.getId(), result.getMenuId());
        assertEquals(menu.getMenuName(), result.getMenuName());
        assertEquals(user.getId(), result.getMemberId());
        assertEquals(user.getUserName(), result.getMemberName());
        assertEquals(savedCart.getQuantity(), result.getQuantity());
    }



    private User createMockCustomer() {
        return new User("일반_유저", UserRole.ROLE_CUSTOMER, "customer@example.com", "password", "서울시 중구", "010-2222-2222");
    }

    private Store createMockStore(User owner) {
        return new Store("땡땡_치킨", "치킨",
                LocalTime.of(9, 0),
                LocalTime.of(22, 0),
                10000.0, owner);
    }

}