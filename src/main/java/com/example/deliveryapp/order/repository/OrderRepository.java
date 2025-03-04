package com.example.deliveryapp.order.repository;

import com.example.deliveryapp.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.store s " +
            "LEFT JOIN FETCH o.member m " +
            "WHERE o.member.id = :userId")
    Order findByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.store s " +
            "LEFT JOIN FETCH o.member m " +
            "WHERE o.member.id = :userId")
    Page<Order> findByUserIdPaged(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.store s " +
            "LEFT JOIN FETCH o.member m " +
            "WHERE o.id = :orderId")
    Order findByOrderId(@Param("orderId") Long orderId);

}
