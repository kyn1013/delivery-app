package com.example.deliveryapp.auth.repository;

import com.example.deliveryapp.auth.entity.RefreshTokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRepository extends JpaRepository<RefreshTokenBlackList, Long> {

    boolean existsByRefreshToken(String refreshToken);
}
