package com.example.deliveryapp.cart.repository;


import com.example.deliveryapp.cart.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c " +
            "LEFT JOIN FETCH c.menu m " +
            "LEFT JOIN FETCH c.user u " +
            "WHERE c.id = :cartId")
    Cart findByIdWithMenuAndMember(@Param("cartId") Long cartId);

    @Query("SELECT c FROM Cart c " +
            "LEFT JOIN FETCH c.menu m " +
            "LEFT JOIN FETCH c.user u " +
            "WHERE c.user.id = :userId")
    Page<Cart> findByMemberId(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT c FROM Cart c " +
            "LEFT JOIN FETCH c.menu m " +
            "LEFT JOIN FETCH c.user u " +
            "LEFT JOIN FETCH c.menu.store s " +
            "WHERE c.user.id = :userId")
    List<Cart> findByUserId(@Param("userId") Long userId);

    void deleteByUserId(Long memberId);

}
