package com.example.deliveryapp.menu.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateMenuException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public DuplicateMenuException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}
