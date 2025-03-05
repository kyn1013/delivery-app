package com.example.deliveryapp.order.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import com.example.deliveryapp.store.entity.Store;
import com.example.deliveryapp.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Order(Long id) {
        this.id = id;
    }

    /* public static Order fromOrderId(Long id) {
        return new Order(id);
    } */
}
