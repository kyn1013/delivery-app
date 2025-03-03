package com.example.deliveryapp.cart.repository;


import com.example.deliveryapp.cart.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c " +
            "LEFT JOIN FETCH c.PMenu m " +
            "LEFT join FETCH c.member u " +
            "WHERE c.id = :cartId")
    Cart findByIdWithMenuAndMember(@Param("cartId") Long cartId);

    @Query("SELECT c FROM Cart c " +
            "LEFT JOIN FETCH c.PMenu m " +
            "LEFT join FETCH c.member u " +
            "WHERE c.member.id = :userId")
    Page<Cart> findByMemberId(Pageable pageable, Long userId);
}
