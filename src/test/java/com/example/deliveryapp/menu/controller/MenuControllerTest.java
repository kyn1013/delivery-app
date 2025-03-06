package com.example.deliveryapp.menu.controller;
import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.menu.dto.request.MenuRequestDto;
import com.example.deliveryapp.menu.dto.request.MenuSimpleUpdateRequestDto;
import com.example.deliveryapp.menu.dto.response.MenuResponseDto;
import com.example.deliveryapp.menu.dto.response.MenuSimpleResponseDto;
import com.example.deliveryapp.menu.enums.MenuCategory;
import com.example.deliveryapp.menu.enums.MenuStatus;
import com.example.deliveryapp.menu.service.MenuService;
import com.example.deliveryapp.user.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

    @WebMvcTest(MenuController.class)
    class MenuControllerTest {

        @MockitoBean
        private MenuService menuService;

        @Autowired
        private ObjectMapper objectMapper;
        @Autowired
        private MockMvc mockMvc;

        @Test
        void 메뉴_생성_성공() throws Exception {
            // given
            MenuRequestDto requestDto = new MenuRequestDto("후라이드_치킨", BigDecimal.valueOf(15000), MenuCategory.MAIN, MenuStatus.AVAILABLE, 10, "바삭바삭한_후라이드_치킨");
            MenuSimpleResponseDto responseDto = new MenuSimpleResponseDto(1L, "후라이드_치킨", BigDecimal.valueOf(15000), MenuCategory.MAIN, 10, "바삭바삭한_후라이드_치킨");
            AuthUser authUser = new AuthUser(1L, "test@example.com", UserRole.ROLE_OWNER);

            given(menuService.saveMenu(any(MenuRequestDto.class), anyLong(), any(AuthUser.class))).willReturn(responseDto);

            // when & then
            mockMvc.perform(post("/api/v1/stores/{storeId}/menus", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.menuName").value("후라이드_치킨"))
                    .andExpect(jsonPath("$.price").value(15000))
                    .andExpect(jsonPath("$.menuCategory").value(MenuCategory.MAIN))
                    .andExpect(jsonPath("$.menuStatus").value("AVAILABLE"));
        }

        @Test
        void 메뉴_목록_조회() throws Exception {
            // given
            long storeId = 1L;
            MenuSimpleResponseDto responseDto = new MenuSimpleResponseDto(1L, "후라이드_치킨", BigDecimal.valueOf(15000), MenuCategory.MAIN, 10,  "바삭바삭한_후라이드_치킨");
            given(menuService.findAvailableMenusByStoreId(storeId)).willReturn(List.of(responseDto));

            // when & then
            mockMvc.perform(get("/api/v1/stores/{storeId}/menus", storeId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].menuName").value("후라이드_치킨"))
                    .andExpect(jsonPath("$[0].price").value(15000))
                    .andExpect(jsonPath("$[0].menuCategory").value(MenuCategory.MAIN))
                    .andExpect(jsonPath("$[0].menuStatus").value(MenuStatus.AVAILABLE));
        }

        @Test
        void 메뉴_Detail_수정_성공() throws Exception {
            // given
            Long menuId = 1L;
            MenuRequestDto requestDto = new MenuRequestDto(
                    "후라이드_치킨_수정",
                    BigDecimal.valueOf(16000),
                    MenuCategory.MAIN,
                    MenuStatus.AVAILABLE,
                    15,
                    "바삭바삭한_후라이드_치킨_수정"
            );

            //업데이트 반영 후
            MenuResponseDto responseDto = new MenuResponseDto(
                    1L,
                    "후라이드_치킨_수정",
                    BigDecimal.valueOf(16000),
                    MenuCategory.MAIN,
                    MenuStatus.AVAILABLE,
                    15,
                    10,  // 재고 수량
                    "바삭바삭한_후라이드_치킨_수정",
                    null,  // createdAt
                    null   // updatedAt
            );

            // 서비스 메서드가 호출되었을 때 responseDto를 반환하도록 설정
            given(menuService.updateMenuDetail(any(MenuRequestDto.class), eq(menuId))).willReturn(responseDto);

            // when & then
            mockMvc.perform(put("/api/v1/stores/{storeId}/menus/{menuId}", 1L, menuId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.menuName").value("후라이드_치킨_수정"))
                    .andExpect(jsonPath("$.price").value(16000))
                    .andExpect(jsonPath("$.menuCategory").value(MenuCategory.MAIN.toString()))  // Enum 값을 string으로 변환하여 비교
                    .andExpect(jsonPath("$.menuStatus").value(MenuStatus.AVAILABLE.toString()));  // Enum 값을 string으로 변환하여 비교
        }

        @Test
        void 메뉴_Simple_수정_성공() throws Exception {
            // given
            long menuId = 1L;
            MenuSimpleUpdateRequestDto requestDto = new MenuSimpleUpdateRequestDto();
            ReflectionTestUtils.setField(requestDto, "MenuStatus", MenuStatus.INACTIVE);
            ReflectionTestUtils.setField(requestDto, "StockQunatity", 20);

            MenuSimpleResponseDto responseDto = new MenuSimpleResponseDto(1L, "후라이드_치킨", BigDecimal.valueOf(15000), MenuCategory.MAIN, 20, "바삭바삭한_후라이드_치킨");

            given(menuService.updateMenuSimple(any(MenuSimpleUpdateRequestDto.class), anyLong())).willReturn(responseDto);

            // when & then
            mockMvc.perform(patch("/api/v1/stores/{storeId}/menus/{menuId}", 1L, menuId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.menuStatus").value(MenuStatus.INACTIVE))
                    .andExpect(jsonPath("$.stockQuantity").value(20));
        }

        @Test
        void 메뉴_삭제_성공() throws Exception {
            // given
            long menuId = 1L;
            AuthUser authUser = new AuthUser(1L, "test@example.com", UserRole.ROLE_OWNER);

            doNothing().when(menuService).delete(menuId);

            // when & then
            mockMvc.perform(delete("/api/v1/stores/{storeId}/menus/{menuId}", 1L, menuId))
                    .andExpect(status().isNoContent());
        }
    }
