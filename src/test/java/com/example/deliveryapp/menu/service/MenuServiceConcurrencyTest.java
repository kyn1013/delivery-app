package com.example.deliveryapp.menu.service;

import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.menu.dto.request.MenuRequestDto;
import com.example.deliveryapp.menu.entity.Menu;
import com.example.deliveryapp.menu.enums.MenuCategory;
import com.example.deliveryapp.menu.enums.MenuStatus;
import com.example.deliveryapp.menu.exception.DuplicateMenuException;
import com.example.deliveryapp.menu.repository.MenuRepository;
import com.example.deliveryapp.store.entity.Store;
import com.example.deliveryapp.store.repository.StoreRepository;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 사용 선언
class MenuServiceConcurrencyTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private MenuService menuService;

    private Store mockStore;
    private AuthUser mockAuthUser;
    private MenuRequestDto mockRequestDto;

    @BeforeEach
    void setUp() {
        User mockOwner = mock(User.class);
        when(mockOwner.getId()).thenReturn(1L);

        mockStore = mock(Store.class);
        when(mockStore.getId()).thenReturn(1L);
        when(mockStore.getOwner()).thenReturn(mockOwner);

        mockAuthUser = new AuthUser(1L, "owner@example.com", UserRole.ROLE_OWNER);
        mockRequestDto = new MenuRequestDto(
                "후라이드 치킨",
                BigDecimal.valueOf(15000),
                MenuCategory.MAIN,
                MenuStatus.AVAILABLE,
                10,
                "바삭한 후라이드 치킨"
        );

        given(storeRepository.findById(anyLong())).willReturn(Optional.of(mockStore));
    }

    @Test
    void 동시에_중복된_메뉴를_저장하면_DuplicateMenuException이_발생한다() throws InterruptedException {
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 메뉴 이름 중복 방지용 랜덤 객체
        Random random = new Random();
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    // 각 스레드마다 메뉴 이름에 고유한 인덱스를 추가하여 중복을 방지
                    int randomNumber = random.nextInt(100);
                    MenuRequestDto modifiedRequestDto = new MenuRequestDto(
                            mockRequestDto.getMenuName() + " " + randomNumber,  // 메뉴 이름에 고유 인덱스를 추가
                            mockRequestDto.getPrice(),
                            mockRequestDto.getMenuCategory(),
                            mockRequestDto.getMenuStatus(),
                            mockRequestDto.getStockQuantity(),
                            mockRequestDto.getDescription()
                    );
                    menuService.saveMenu(modifiedRequestDto, mockStore.getId(), mockAuthUser);
                } catch (DuplicateMenuException ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        // 각 호출에 대해서 정확한 횟수를 검증
        verify(menuRepository, times(threadCount)).existsByStoreIdAndMenuNameAndMenuStatusNot(anyLong(), anyString(), eq(MenuStatus.DELETED));
        verify(menuRepository, times(1)).save(any(Menu.class));  // 메뉴 저장은 한 번만 발생해야 함
    }
}
