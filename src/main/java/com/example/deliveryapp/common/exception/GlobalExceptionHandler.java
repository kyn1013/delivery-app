package com.example.deliveryapp.common.exception;

import com.example.deliveryapp.auth.common.exception.ErrorResponse;
import com.example.deliveryapp.menu.exception.DuplicateMenuException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 메뉴 domain CustomException
    @ExceptionHandler(DuplicateMenuException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateMenuException(DuplicateMenuException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    // 입력값 dto 검증 에러 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String firstErrorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElseThrow(() -> new IllegalStateException("검증 에러가 존재해야 합니다."));

        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, firstErrorMessage);
    }

    // httpbody가 입력값 dto로 변환이 실패한 경우 검증 에러 핸들러
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String errorMessage = "요청 데이터타입이 올바르지 않습니다.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, errorMessage);
    }

    public ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.name());
        errorResponse.put("code", status.value());
        errorResponse.put("message", message);

        return new ResponseEntity<>(errorResponse, status);
    }
}
