package com.example.deliveryapp.menu.repository;

import com.example.deliveryapp.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
