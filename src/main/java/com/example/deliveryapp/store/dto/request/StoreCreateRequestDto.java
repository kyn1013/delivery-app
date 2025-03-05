package com.example.deliveryapp.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StoreCreateRequestDto {

    @NotBlank(message = "가게 이름은 필수 항목입니다.")
    private String businessName;

    @NotBlank(message = "카테고리는 필수 항목입니다.")
    private String category;

    @NotNull(message = "오픈 시간은 필수 항목입니다.")
    private LocalDateTime openingTime;

    @NotNull(message = "마감 시간은 필수 항목입니다.")
    private LocalDateTime closingTime;

    @Positive(message = "최소 주문 금액은 0보다 커야 합니다.")
    private Double minOrderPrice;
}