package com.example.deliveryapp.review.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import com.example.deliveryapp.order.entity.Order;
import com.example.deliveryapp.store.entity.Store;
import com.example.deliveryapp.user.entity.User;
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


    /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; */


    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private String content;

    public Review(/* User user,
                  Store store,
                  Order order, */
                  int score,
                  String content) {
        /* this.user = user;
        this.store = store;
        this.order = order; */
        this.score = score;
        this.content = content;
    }

    public void update(String content, int score) {
        this.content = content;
        this.score = score;
    }
/*
    // 정적 팩토리 메서드 추가: 외래키 관계를 처리하기 위한 팩토리 메서드들
    // 1. User 엔티티의 ID로 User 객체 생성 (추후 User 엔티티가 완성되면 삭제)
    public static User fromUserId(Long id) {
        return new User(id); // User 엔티티가 구현되면 해당 ID를 기준으로 객체 생성
    }

    // 2. Store 엔티티의 ID로 Store 객체 생성 (추후 Store 엔티티가 완성되면 삭제)
    public static Store fromStoreId(Long id) {
        return new Store(id); // Store 엔티티가 구현되면 해당 ID를 기준으로 객체 생성
    }

    // 3. Order 엔티티의 ID로 Order 객체 생성 (추후 Order 엔티티가 완성되면 삭제)
    public static Order fromOrderId(Long id) {
        return new Order(id); // Order 엔티티가 구현되면 해당 ID를 기준으로 객체 생성
    }

 */
}
