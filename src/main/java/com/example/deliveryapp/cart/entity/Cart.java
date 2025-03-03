package com.example.deliveryapp.cart.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import com.example.deliveryapp.order.pratice_entity.Member;
import com.example.deliveryapp.order.pratice_entity.PMenu;
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
    private PMenu PMenu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Long quantity;

    public Cart(PMenu PMenu, Member member, Long quantity) {
        this.PMenu = PMenu;
        this.member = member;
        this.quantity = quantity;
    }

}
