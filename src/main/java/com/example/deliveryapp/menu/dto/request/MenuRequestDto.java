package com.example.deliveryapp.menu.dto.request;

import com.example.deliveryapp.menu.enums.MenuCategory;
import com.example.deliveryapp.menu.enums.MenuStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class MenuRequestDto {

    private String menuName;

    private BigDecimal price;

    private MenuCategory menuCategory;

    private MenuStatus menuStatus;

    private Integer stockQuantity;

    private String description;
}
