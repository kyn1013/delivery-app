package com.example.deliveryapp.menu.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "menu")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
