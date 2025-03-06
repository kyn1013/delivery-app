package com.example.deliveryapp.store.service;

import com.example.deliveryapp.menu.repository.MenuRepository;
import com.example.deliveryapp.order.repository.OrderDetailRepository;
import com.example.deliveryapp.order.repository.OrderRepository;
import com.example.deliveryapp.review.repository.ReviewRepository;
import com.example.deliveryapp.store.dto.request.StoreCreateRequestDto;
import com.example.deliveryapp.store.dto.request.StoreUpdateRequestDto;
import com.example.deliveryapp.store.dto.response.StoreResponseDto;
import com.example.deliveryapp.store.entity.Store;
import com.example.deliveryapp.store.repository.StoreRepository;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.enums.UserRole;
import com.example.deliveryapp.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StoreService storeService;

    // 가게 생성 테스트
    @Test
    void 가게_생성_성공() {
        // given
        StoreCreateRequestDto requestDto = new StoreCreateRequestDto("Test Store", "Food",
                LocalTime.parse("09:00"),  LocalTime.parse("21:00"), 10000.0, "Seoul");

        // User 객체 생성 시 사장님용 생성자에 맞는 값들 입력
        User owner = new User("owner", UserRole.ROLE_OWNER, "owner@example.com", "password", "1234567890", "010-1234-5678");

        // owner 조회 시 owner 객체를 반환하도록 설정
        given(userRepository.findByEmail("owner@example.com")).willReturn(Optional.of(owner));
        given(storeRepository.countByOwnerId(owner.getId())).willReturn(0); // 가게가 3개 미만일 경우
        given(storeRepository.save(any(Store.class))).willAnswer(invocation -> {
            Store store = invocation.getArgument(0);
            store.setOwner(owner); // Store 객체에 owner 설정
            return store;
        });

        // when
        StoreResponseDto storeResponseDto = storeService.createStore(requestDto, "owner@example.com");

        // then
        assertNotNull(storeResponseDto); // 가게 생성 후 null이 아니어야 한다
        assertEquals("Test Store", storeResponseDto.getBusinessName()); // 생성된 가게 이름이 "Test Store"이어야 한다
    }

    // 가게 단건 조회 테스트
    @Test
    void 가게_단건_조회_성공() {
        // given
        User owner = new User("owner", UserRole.ROLE_OWNER, "owner@example.com", "password", "1234567890", "010-1234-5678");

        Store store = new Store();
        store.setBusinessName("Test Store");
        store.setOwner(owner);

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        // when
        StoreResponseDto storeResponseDto = storeService.findStoreById(1L);

        // then
        assertNotNull(storeResponseDto); // null이 아닌지 확인
        assertEquals("Test Store", storeResponseDto.getBusinessName()); // 가게 이름이 "Test Store"인지 확인
    }

    // 가게 단건 조회 실패 테스트 (가게를 찾을 수 없는 경우)
    @Test
    void 가게_단건_조회_실패() {
        // given
        given(storeRepository.findById(1L)).willReturn(Optional.empty()); // 가게가 존재하지 않음

        // when & then
        assertThrows(IllegalArgumentException.class, () -> storeService.findStoreById(1L)); // 예외 발생해야 함
    }

    // 가게명으로 검색 테스트
    @Test
    void 가게_검색_성공() {
        // given
        User owner = new User("owner", UserRole.ROLE_OWNER, "owner@example.com", "password", "1234567890", "010-1234-5678");

        // Store 객체 생성 후 owner 설정 (store에는 owner가 없지만, 이 owner를 가져올 수 있게 처리)
        Store store1 = new Store();
        store1.setBusinessName("Test Store");
        store1.setOwner(owner);  // owner 설정 (User 객체가 Store의 owner 역할을 할 수 있도록)

        Store store2 = new Store();
        store2.setBusinessName("Another Store");
        store2.setOwner(owner);  // owner 설정

        // Repository에서 "Store"를 포함하는 가게 2개를 찾도록 설정
        given(storeRepository.findByBusinessNameContaining("Store")).willReturn(List.of(store1, store2));

        // when
        List<StoreResponseDto> stores = storeService.searchStores("Store");

        // then
        assertNotNull(stores); // null이 아니어야 한다
        assertEquals(2, stores.size()); // "Store"를 포함하는 가게가 2개여야 한다
        assertEquals("Test Store", stores.get(0).getBusinessName()); // 첫 번째 가게 이름이 "Test Store"여야 한다
        assertEquals("Another Store", stores.get(1).getBusinessName()); // 두 번째 가게 이름이 "Another Store"여야 한다
    }

    // 가게명으로 검색 실패 (검색 결과가 없는 경우)
    @Test
    void 가게_검색_실패() {
        // given
        given(storeRepository.findByBusinessNameContaining("NonExistentStore")).willReturn(List.of()); // 결과가 없음

        // when
        List<StoreResponseDto> stores = storeService.searchStores("NonExistentStore");

        // then
        assertNotNull(stores);
        assertTrue(stores.isEmpty());
    }

    // 가게 수정 테스트
    @Test
    void 가게_수정_성공() {
        // given
        User owner = new User("owner", UserRole.ROLE_OWNER, "owner@example.com", "password", "1234567890", "010-1234-5678");

        // Store 객체 생성 후 owner 설정
        Store store = new Store();
        store.setId(1L);
        store.setBusinessName("Test Store");
        store.setOwner(owner);  // owner 설정

        // store 업데이트를 위한 DTO 생성 (ownerUsername을 직접 설정)
        StoreUpdateRequestDto storeUpdateRequestDto = new StoreUpdateRequestDto("Updated Store", "Category", LocalTime.of(9, 0), LocalTime.of(21, 0), 1000.0, "New Address");
        storeUpdateRequestDto.setBusinessName("Updated Store");

        // StoreService의 storeRepository mock 설정
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(storeRepository.save(any(Store.class))).willReturn(store);

        // when
        StoreResponseDto storeResponseDto = storeService.updateStore(1L, storeUpdateRequestDto, owner);

        // then
        assertNotNull(storeResponseDto);
        assertEquals("Updated Store", storeResponseDto.getBusinessName());
        assertEquals(owner.getUserName(), storeResponseDto.getOwnerUsername());  // 업데이트 후 owner username 확인
    }






    // 가게 수정 실패 (본인 가게가 아닌 경우)
    @Test
    void 가게_수정_실패_권한() {
        // given
        StoreUpdateRequestDto updateDto = new StoreUpdateRequestDto("Updated Store", "Fast Food",
                LocalTime.parse("10:00"), LocalTime.parse("22:00"), 15000.0, "Busan");

        User owner = new User("owner@example.com", UserRole.ROLE_OWNER, "owner@example.com", "password", "ownerPhoneNumber");
        User otherUser = new User("other@example.com", UserRole.ROLE_OWNER, "other@example.com", "password", "otherPhoneNumber");

        Store store = new Store();
        store.setOwner(owner);
        store.setId(1L);

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> storeService.updateStore(1L, updateDto, otherUser));
    }

    // 가게 삭제 테스트
    @Test
    void 가게_삭제_성공() {
        // given
        User owner = new User("owner@example.com", UserRole.ROLE_OWNER, "owner@example.com", "password", "ownerPhoneNumber");
        Store store = new Store();
        store.setId(1L);
        store.setOwner(owner);

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        // when
        storeService.deleteStore(1L, owner);

        // then
        verify(storeRepository).delete(store); // 가게가 삭제되었는지 검증

        // 메뉴, 주문, 주문상세, 리뷰 삭제 메서드도 호출되었는지 검증
        verify(menuRepository).deleteByStore(store);
        verify(orderRepository).deleteByStore(store);
        verify(orderDetailRepository).deleteByMenuStore(store);
        verify(reviewRepository).deleteByStore(store);
    }


    // 가게 삭제 실패 (본인 가게가 아닌 경우)
    @Test
    void 가게_삭제_실패_권한() {
        // given
        User owner = new User("owner@example.com", UserRole.ROLE_OWNER, "owner@example.com", "password", "ownerPhoneNumber");
        User otherUser = new User("other@example.com", UserRole.ROLE_OWNER, "other@example.com", "password", "otherPhoneNumber");

        Store store = new Store();
        store.setId(1L);
        store.setOwner(owner);

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        // when & then
        assertThrows(IllegalStateException.class, () -> storeService.deleteStore(1L, otherUser)); // 권한이 다른 사용자가 삭제를 시도할 때 예외 발생
    }
}



