package com.example.deliveryapp.menu.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import com.example.deliveryapp.menu.enums.MenuCategory;
import com.example.deliveryapp.menu.enums.MenuStatus;
import com.example.deliveryapp.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "menu")
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;

    private String menuName;

    @Enumerated(EnumType.STRING)
    private MenuCategory menuCategory;

    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="store_id")
    private Store store;

    private Integer stockQuantity;

    private String description;


}
