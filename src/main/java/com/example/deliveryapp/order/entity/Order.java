package com.example.deliveryapp.order.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import com.example.deliveryapp.order.enums.OrderStatus;
import com.example.deliveryapp.order.enums.OrderStatusConverter;
import com.example.deliveryapp.store.entity.Store;
import com.example.deliveryapp.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.Builder;

import java.math.BigDecimal;


@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private String orderNumber;

    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus state;

    private BigDecimal totalPrice;

    @Builder
    public Order(User user, Store store, String orderNumber, OrderStatus state) {
        this.user = user;
        this.store = store;
        this.orderNumber = orderNumber;
        this.state = state;
    }

    public void update(OrderStatus state) {
        this.state = state;
    }

    public void updateTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}


