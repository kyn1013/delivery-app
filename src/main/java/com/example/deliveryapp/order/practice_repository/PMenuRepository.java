package com.example.deliveryapp.order.practice_repository;

import com.example.deliveryapp.order.pratice_entity.PMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PMenuRepository extends JpaRepository<PMenu, Long> {
}
