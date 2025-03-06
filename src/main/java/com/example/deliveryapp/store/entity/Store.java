package com.example.deliveryapp.store.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import com.example.deliveryapp.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "store")
public class Store extends BaseEntity {  // BaseEntity 상속

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String businessName;

    private String category;

    private LocalTime openingTime;

    private LocalTime closingTime;


    private Double minOrderPrice;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;  // 가게의 주인(사장님)

    private String address;
}