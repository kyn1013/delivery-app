package com.example.deliveryapp.auth.common.util;

import com.example.deliveryapp.auth.entity.AuthUser;
import com.example.deliveryapp.common.annotation.Auth;
import com.example.deliveryapp.user.enums.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


// AOP를 이용해 현재 로그인 유저가 OWNER인지 확인, @OwnerUser 애노테이션으로 사용가능. ,Aop를 쓸지 interceptor를 사용하지 물어보기.
@Slf4j
@Aspect
@Component
public class OwnerUserAspect {

    private final AuthUser authUser;

    public OwnerUserAspect(@Auth AuthUser authUser) {
        this.authUser = authUser;
    }

    @Around("@annotation(com.example.deliveryapp.common.annotation.OwnerUser)")
    public Object checkOwner(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("유저 역활 OWNER인지 체크");

        if (authUser.getUserRole() != UserRole.ROLE_OWNER) {
            throw new IllegalAccessException("ROLE_OWNER 유저가 아닙니다.");
        }

        return joinPoint.proceed();
    }
}
