package com.example.deliveryapp.common.exception;

import com.example.deliveryapp.auth.common.exception.ErrorResponse;
import com.example.deliveryapp.menu.exception.DuplicateMenuException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 메뉴 domain CustomException
    @ExceptionHandler(DuplicateMenuException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateMenuException(DuplicateMenuException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }
}
