package com.example.deliveryapp.review.controller;

import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.common.annotation.Auth;
import com.example.deliveryapp.review.dto.request.ReviewUpdateRequestDto;
import com.example.deliveryapp.review.dto.response.ReviewResponseDto;
import com.example.deliveryapp.review.dto.request.ReviewSaveRequestDto;
import com.example.deliveryapp.review.dto.response.ReviewSaveResponseDto;
import com.example.deliveryapp.review.dto.response.ReviewUpdateResponseDto;
import com.example.deliveryapp.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping("/api/v1/reviews")
    public ResponseEntity<ReviewSaveResponseDto> save (
            @Auth AuthUser user,
            @RequestBody ReviewSaveRequestDto dto
    ) {
        Long userId = user.getId();
        dto.setUserId(userId);
        return ResponseEntity.ok(reviewService.save(dto));
    }

    // 리뷰 조회
    @GetMapping("/api/v1/reviews")
    public ResponseEntity<List<ReviewResponseDto>> findAll(/* Long userId, Long storeId, Long orderId */) {
        return ResponseEntity.ok(reviewService.findAll(/* userId, storeId, orderId */));
    }

    // 리뷰 수정
    @PatchMapping("/api/v1/review/{id}")
    public ResponseEntity<ReviewUpdateResponseDto> update (
            @Auth AuthUser user, // JWT 토큰 검증
            @RequestBody ReviewUpdateRequestDto dto,
            @PathVariable /* Long storeId, Long orderId, */ Long id
    ) {
        return ResponseEntity.ok(reviewService.update(dto, id, user.getId() /*,  store.getId(), order.getId */));
    }

    // 리뷰 삭제

    @DeleteMapping("/api/v1/review/{id}")
    public void delete(@Auth AuthUser user, // JWT 토큰 검증
                       @PathVariable Long id) {
        reviewService.deleteById(user.getId(), id);
    }

}
