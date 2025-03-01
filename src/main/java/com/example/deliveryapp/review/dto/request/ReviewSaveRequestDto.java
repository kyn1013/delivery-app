package com.example.deliveryapp.review.dto.request;

import lombok.Getter;

@Getter
public class ReviewSaveRequestDto {

    private Long storeId;
    private Long orderId;
    private Long memberId;
    private int score;
    private String content;


}
