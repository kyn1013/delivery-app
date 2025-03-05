package com.example.deliveryapp.menu.dto.request;

import com.example.deliveryapp.menu.enums.MenuStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuSimpleUpdateRequestDto {

    private MenuStatus menuStatus;

    private Integer stockQuantity;
}
