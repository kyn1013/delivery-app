package com.example.deliveryapp.review.controller;

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

    @PostMapping("/api/v1/reviews")
    public ResponseEntity<ReviewSaveResponseDto> save (@RequestBody ReviewSaveRequestDto dto /*, Long userId, Long storeId, Long orderId */) {
        return ResponseEntity.ok(reviewService.save(/* userId, storeId, orderId, */ dto));
    }

    @GetMapping("/api/v1/reviews")
    public ResponseEntity<List<ReviewResponseDto>> findAll(/* Long userId, Long storeId, Long orderId */) {
        return ResponseEntity.ok(reviewService.findAll(/* userId, storeId, orderId */));
    }

    @PatchMapping("/api/v1/review/{id}")
    public ResponseEntity<ReviewUpdateResponseDto> update (
            @RequestBody ReviewUpdateRequestDto dto,
            @PathVariable /* Long storeId, Long orderId, */ Long id
    ) {
        return ResponseEntity.ok(reviewService.update(/* storeId, orderId, */ dto, id));
    }

    @DeleteMapping("/api/v1/review/{id}")
    public void delete(@PathVariable Long id) {
        reviewService.deleteById(id);
    }

}
