package com.example.deliveryapp.store.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Store extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Store(Long id) {
        this.id = id;
    }

    /* public static Store fromStoreId(Long id) {
        return new Store(id);
    } */
}
