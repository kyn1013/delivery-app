package com.example.deliveryapp.auth.entity;

import com.example.deliveryapp.user.enums.IsDeleted;
import com.example.deliveryapp.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Component
@RequestScope
// OwnerUserAspect.class에 주입하려다보니
// Component여야 했고, 싱글빈이면 인증문제 발생 => RequestScope, 기능저하 문제 AuthUserProvider를 만드는게 더 좋을지 튜터님께 물어볼것.
public class AuthUser {

    private final Long id;

    private final String email;

    private final UserRole userRole;

    public AuthUser(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }
}
