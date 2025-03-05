package com.example.deliveryapp.auth.common.util;

import com.example.deliveryapp.common.annotation.EnumCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 요청의 enum값을 유효성을 검증하는 클래스
 */
public class EnumValidator implements ConstraintValidator<EnumCheck, String> {

    private EnumCheck annotation;

    @Override
    public void initialize(EnumCheck annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }

        // Enum 클래스 정보를 가져와서 해당 Enum에 포함된 값들 중 하나인지 확인
        Class<? extends Enum<?>> enumClass = annotation.enumClass();
        Enum<?>[] enumValues = enumClass.getEnumConstants();
        for (Enum<?> enumValue : enumValues) {
            if (enumValue.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
