package com.example.deliveryapp.review.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewUpdateResponseDto {

    private final Long id;
    private final Long sotreId;
//    private final Long orderId;
    private final int score;
    private final String content;
    private final LocalDateTime updatedAt;

    public ReviewUpdateResponseDto(Long id,
                                   Long sotreId,
//                                   Long orderId,
                                   int score,
                                   String content,
                                   LocalDateTime updatedAt) {
        this.id = id;
        this.sotreId = sotreId;
//        this.orderId = orderId;
        this.score = score;
        this.content = content;
        this.updatedAt = updatedAt;
    }
}
