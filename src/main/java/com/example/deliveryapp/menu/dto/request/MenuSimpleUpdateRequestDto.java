package com.example.deliveryapp.menu.dto.request;

import com.example.deliveryapp.common.annotation.EnumCheck;
import com.example.deliveryapp.menu.enums.MenuStatus;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuSimpleUpdateRequestDto {

    @EnumCheck(enumClass = MenuStatus.class, message = "유효하지 않은 메뉴 상태입니다.")
    private MenuStatus menuStatus;

    @Min(value = 0, message = "재고 수량은 0이상이어야 합니다.")
    private Integer stockQuantity;
}
