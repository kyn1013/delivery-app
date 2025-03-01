package com.example.deliveryapp.review.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewSaveResponseDto {

    private final String content;
    private final int score;
    private final LocalDateTime createdAt;

    public ReviewSaveResponseDto(String content, int score, LocalDateTime createdAt) {
        this.content = content;
        this.score = score;
        this.createdAt = createdAt;
    }
}
