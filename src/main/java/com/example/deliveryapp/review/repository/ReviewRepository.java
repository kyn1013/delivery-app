package com.example.deliveryapp.review.repository;

import com.example.deliveryapp.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
