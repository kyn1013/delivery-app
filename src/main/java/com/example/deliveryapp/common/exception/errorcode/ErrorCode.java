package com.example.deliveryapp.common.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 Bad Request (잘못된 요청)
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    EMPTY_REVIEW_CONTENT(HttpStatus.BAD_REQUEST, "리뷰를 입력하세요."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),

    // 403 Forbidden (권한 없음)
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    UNAUTHORIZED_REVIEW_UPDATE(HttpStatus.FORBIDDEN, "리뷰 수정 권한이 없습니다."),
    UNAUTHORIZED_REVIEW_DELETE(HttpStatus.FORBIDDEN, "리뷰 삭제 권한이 없습니다."),

    // 404 Not Found (리소스 없음)
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물이 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다.");
    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;

    }

}
