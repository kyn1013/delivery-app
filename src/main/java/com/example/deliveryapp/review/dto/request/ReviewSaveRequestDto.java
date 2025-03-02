package com.example.deliveryapp.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewSaveRequestDto {

    @NotBlank(message = "내용을 입력하세요.")
    private String content;


    @NotNull(message = "별점을 선택하세요.")
    private int score;


}
