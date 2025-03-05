package com.example.deliveryapp.cart.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import com.example.deliveryapp.menu.entity.Menu;
import com.example.deliveryapp.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "cart")
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long quantity;

    public Cart(Menu menu, User user, Long quantity) {
        this.menu = menu;
        this.user = user;
        this.quantity = quantity;
    }

    public void update(Menu menu, Long quantity){
        this.menu = menu;
        this.quantity = quantity;
    }

}
