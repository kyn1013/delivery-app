package com.example.deliveryapp.store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@SuppressWarnings("all") // 만들어질 때까지 경고 무시
@Entity
public abstract class Store {
    @Id
    private Long id;
}
