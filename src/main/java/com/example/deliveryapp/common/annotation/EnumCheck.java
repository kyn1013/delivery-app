package com.example.deliveryapp.common.annotation;

import com.example.deliveryapp.auth.common.util.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 올바르지 않은 enum값이 요청으로 들어올 때, 방어코드
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface EnumCheck {
    String message() default "올바르지 않은 요청입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // Enum 클래스 정보를 파라미터로 받을 수 있도록 설정
    Class<? extends Enum<?>> enumClass();
}
