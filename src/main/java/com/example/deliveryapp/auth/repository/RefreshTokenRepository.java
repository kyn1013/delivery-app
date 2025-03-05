package com.example.deliveryapp.auth.repository;

import com.example.deliveryapp.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    boolean existsByRefreshToken(String token);
}
