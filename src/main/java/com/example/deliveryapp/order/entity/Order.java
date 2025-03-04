package com.example.deliveryapp.order.entity;


import com.example.deliveryapp.common.entity.BaseEntity;
import com.example.deliveryapp.order.pratice_entity.Member;
import com.example.deliveryapp.order.pratice_entity.Store;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private String orderNumber;

    private String state;

    @Builder
    public Order(Member member, Store store, String orderNumber, String state) {
        this.member = member;
        this.store = store;
        this.orderNumber = orderNumber;
        this.state = state;
    }

    public void update(String state){
        this.state = state;
    }

}
