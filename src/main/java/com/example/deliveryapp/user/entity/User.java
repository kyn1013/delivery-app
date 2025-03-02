package com.example.deliveryapp.user.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public User(Long id) {
        this.id = id;
    }

    /*public static User fromUserId(Long id) {
        return new User(id);
    } */
}
