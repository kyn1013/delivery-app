package com.example.deliveryapp.review.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewSaveResponseDto {

    private final Long id;
    private final String content;
    private final int score;
    private final LocalDateTime createdAt;

    public ReviewSaveResponseDto(Long id, String content, int score, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.score = score;
        this.createdAt = createdAt;
    }
}
