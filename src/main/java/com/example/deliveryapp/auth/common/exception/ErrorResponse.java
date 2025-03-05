package com.example.deliveryapp.auth.common.exception;

import lombok.Getter;

/**
 * 에러 응답 형식 확장, 추가 정보 제공을 위한 class
 */
@Getter
public class ErrorResponse {
    private final String errorCode;
    private final String message;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
