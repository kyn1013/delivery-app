package com.example.deliveryapp.review.service;

import com.example.deliveryapp.common.exception.errorcode.CustomException;
import com.example.deliveryapp.review.dto.request.ReviewUpdateRequestDto;
import com.example.deliveryapp.review.dto.response.ReviewResponseDto;
import com.example.deliveryapp.review.dto.request.ReviewSaveRequestDto;
import com.example.deliveryapp.review.dto.response.ReviewSaveResponseDto;
import com.example.deliveryapp.review.dto.response.ReviewUpdateResponseDto;
import com.example.deliveryapp.review.entity.Review;
import com.example.deliveryapp.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.deliveryapp.common.exception.errorcode.ErrorCode.INVALID_INPUT_VALUE;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewSaveResponseDto save(ReviewSaveRequestDto dto) {
        Review review = new Review(dto.getStoreId(),
                dto.getOrderId(),
                dto.getId(),
                dto.getScore(),
                dto.getContent()
                );
        Review savedReview = reviewRepository.save(review);
        return new ReviewSaveResponseDto(savedReview.getId(),
                savedReview.getContent(),
                savedReview.getScore(),
                savedReview.getCreatedAt()
        );
    }

    @Transactional(readOnly = false)
    public List<ReviewResponseDto> findAll() {

        List<Review> reviews = reviewRepository.findAll();

        List<ReviewResponseDto> dtos = new ArrayList<>();

        for (Review review : reviews) {
            ReviewResponseDto dto = new ReviewResponseDto(review.getId(),
                    review.getStoreId(),
                    review.getOrderId(),
                    review.getScore(),
                    review.getContent(),
                    review.getCreatedAt(),
                    review.getUpdatedAt());
            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional
    public ReviewUpdateResponseDto update(ReviewUpdateRequestDto dto, Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(INVALID_INPUT_VALUE));
        review.update(dto.getContent(), dto.getScore());
        return  new ReviewUpdateResponseDto(review.getId(),
                review.getStoreId(),
                review.getOrderId(),
                review.getScore(),
                review.getContent(),
                review.getUpdatedAt()
        );

    }

    @Transactional
    public void deleteById(Long id) {
            if (!reviewRepository.existsById(id)) {
                throw new IllegalArgumentException("삭제할 리뷰가 없습니다.");
            }
            reviewRepository.deleteById(id);
        }
    }

