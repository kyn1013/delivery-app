package com.example.deliveryapp.menu.dto.response;

import com.example.deliveryapp.menu.entity.Menu;
import com.example.deliveryapp.menu.enums.MenuCategory;
import com.example.deliveryapp.menu.enums.MenuStatus;
import com.example.deliveryapp.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MenuDetailResponseDto {

    private final Long id;
    private final String menuName;
    private final BigDecimal price;
    private final MenuCategory menuCategory;
    private final MenuStatus menuStatus;
    private final Integer salesCount;
    private final Integer stockQunatity;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static MenuDetailResponseDto toDto(Menu menu) {
        return MenuDetailResponseDto.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuCategory(menu.getMenuCategory())
                .menuStatus(menu.getMenuStatus())
                .salesCount(menu.getSalesCount())
                .stockQunatity(menu.getStockQuantity())
                .description(menu.getDescription())
                .createdAt(menu.getCreatedAt())
                .updatedAt(menu.getUpdatedAt())
                .build();
    }
}
