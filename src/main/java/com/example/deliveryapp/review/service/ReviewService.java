package com.example.deliveryapp.review.service;

import com.example.deliveryapp.common.exception.errorcode.CustomException;
import com.example.deliveryapp.order.entity.Order;
import com.example.deliveryapp.review.dto.request.ReviewUpdateRequestDto;
import com.example.deliveryapp.review.dto.response.ReviewResponseDto;
import com.example.deliveryapp.review.dto.request.ReviewSaveRequestDto;
import com.example.deliveryapp.review.dto.response.ReviewSaveResponseDto;
import com.example.deliveryapp.review.dto.response.ReviewUpdateResponseDto;
import com.example.deliveryapp.review.entity.Review;
import com.example.deliveryapp.review.repository.ReviewRepository;
import com.example.deliveryapp.store.entity.Store;
import com.example.deliveryapp.user.entity.User;
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
    public ReviewSaveResponseDto save(/* Long userId, Long storeId, Long orderId, */ ReviewSaveRequestDto dto) {
        /* User user = User.fromUserId(userId);
        Store store = Store.fromStoreId(storeId);
        Order order = Order.fromOrderId(orderId); */
        Review review = new Review(/* user,
                store,
                order, */
                dto.getScore(),
                dto.getContent()
                );
        Review savedReview = reviewRepository.save(review);
        return new ReviewSaveResponseDto(savedReview.getId(),
                /* user.getId(),
                store.getId(),
                order.getId(), */
                savedReview.getContent(),
                savedReview.getScore(),
                savedReview.getCreatedAt()
        );
    }

    @Transactional(readOnly = false)
    public List<ReviewResponseDto> findAll(/* Long userId, Long storeId, Long orderId */) {
        /* User user = User.fromUserId(userId);
        Store store = Store.fromStoreId(storeId);
        Order order = Order.fromOrderId(orderId); */

        List<Review> reviews = reviewRepository.findAll();

        List<ReviewResponseDto> dtos = new ArrayList<>();

        for (Review review : reviews) {
            ReviewResponseDto dto = new ReviewResponseDto(review.getId(),
                   /* user.getId(),
                    store.getId(),
                    order.getId(), */
                    review.getScore(),
                    review.getContent(),
                    review.getCreatedAt(),
                    review.getUpdatedAt());
            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional
    public ReviewUpdateResponseDto update(/* Long storeId, Long orderId, */ ReviewUpdateRequestDto dto, Long id) {
       /* Store store = Store.fromStoreId(storeId);
        Order order = Order.fromOrderId(orderId); */

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(INVALID_INPUT_VALUE));
        review.update(dto.getContent(), dto.getScore());
        return  new ReviewUpdateResponseDto(review.getId(),
               /*  store.getId(),
                order.getId(), */
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

