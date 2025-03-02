package com.example.deliveryapp.review.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private Long storeId;
    private Long orderId;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private String content;

    public Review(Long memberId,
                  Long storeId,
                  Long orderId,
                  int score,
                  String content
    ) {
        this.memberId = memberId;
        this.storeId = storeId;
        this.orderId = orderId;
        this.score = score;
        this.content = content;
    }

    public void update(String content, int score) {
        this.content = content;
        this.score = score;
    }
}
