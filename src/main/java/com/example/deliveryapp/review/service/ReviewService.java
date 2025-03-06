package com.example.deliveryapp.review.service;

import com.example.deliveryapp.common.exception.custom_exception.CustomException;
import com.example.deliveryapp.common.exception.errorcode.ErrorCode;
import com.example.deliveryapp.review.dto.request.ReviewUpdateRequestDto;
import com.example.deliveryapp.review.dto.response.ReviewResponseDto;
import com.example.deliveryapp.review.dto.request.ReviewSaveRequestDto;
import com.example.deliveryapp.review.dto.response.ReviewSaveResponseDto;
import com.example.deliveryapp.review.dto.response.ReviewUpdateResponseDto;
import com.example.deliveryapp.review.entity.Review;
import com.example.deliveryapp.review.repository.ReviewRepository;
import com.example.deliveryapp.user.entity.User;
import com.example.deliveryapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
//  private final OrderRepository orderRepository; //주문 정보 가져오기 위함
//  private final StoreRepository storeRepository; //가게 정보 가져오기 위함

    @Transactional
    public ReviewSaveResponseDto save(ReviewSaveRequestDto dto) {
        Long userId = dto.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        /* Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new CustomException(INVALID_INPUT_VALUE));

       /* Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new CustomException(INVALID_INPUT_VALUE));

            // 주문 상태 체크: 완료된 주문만 리뷰 작성 가능
            if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
                throw new IllegalStateException("배달 완료된 주문만 리뷰를 작성할 수 있습니다.");
            } */ // order 구현 뒤 주석 제거
        Review review = new Review(user,
                /*store,
                order, */
                dto.getScore(),
                dto.getContent()
                );
        Review savedReview = reviewRepository.save(review);

        return new ReviewSaveResponseDto(
                savedReview.getId(),
                user.getId(),
                /* store.getId(),
                order.getId(), */
                savedReview.getContent(),
                savedReview.getScore(),
                savedReview.getCreatedAt()
        );
    }

    // 리뷰 조회
    @Transactional(readOnly = false)
    public List<ReviewResponseDto> findAll() {
        // 리뷰 최신순으로 정렬

        List<Review> reviews = reviewRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));

        List<ReviewResponseDto> dtos = new ArrayList<>();

        for (Review review : reviews) {
            ReviewResponseDto dto = new ReviewResponseDto(review.getId(),
                   review.getUser().getId(),
                    /* store.getId(),
                    order.getId(), */
                    review.getScore(),
                    review.getContent(),
                    review.getCreatedAt(),
                    review.getUpdatedAt());
            dtos.add(dto);
        }

        return dtos;
    }

    // 리뷰 수정
    @Transactional
    public ReviewUpdateResponseDto update(/* Long storeId, Long orderId, */ ReviewUpdateRequestDto dto, Long id, Long userId) {
        // 리뷰 존재 여부 확인
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        //작성자 검증
        if (!review.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REVIEW_UPDATE);
        }

        review.update(dto.getContent(), dto.getScore());
        return  new ReviewUpdateResponseDto(review.getId(),
               /*  store.getId(),
                order.getId(), */
                review.getScore(),
                review.getContent(),
                review.getUpdatedAt()
        );

    }

    // 리뷰 삭제
    @Transactional
    public void deleteById(Long id, Long userId) {
        // 리뷰 존재 여부 확인
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        //작성자 검증
        if (!review.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REVIEW_DELETE);
        }
        // 리뷰 삭제
        reviewRepository.deleteById(id);
        }
    }

