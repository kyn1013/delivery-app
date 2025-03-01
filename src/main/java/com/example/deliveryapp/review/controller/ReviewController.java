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
    public ResponseEntity<ReviewSaveResponseDto> save (@RequestBody ReviewSaveRequestDto dto) {
        return ResponseEntity.ok(reviewService.save(dto));
    }

    @GetMapping("/api/v1/reviews")
    public ResponseEntity<List<ReviewResponseDto>> findAll() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    @PatchMapping("/api/v1/review/{id}")
    public ResponseEntity<ReviewUpdateResponseDto> update (@RequestBody ReviewUpdateRequestDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.update(dto, id));
    }

}
