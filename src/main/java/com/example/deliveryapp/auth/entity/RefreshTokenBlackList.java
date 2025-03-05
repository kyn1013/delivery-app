package com.example.deliveryapp.auth.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshTokenBlackList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String refreshToken;

    public RefreshTokenBlackList(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
