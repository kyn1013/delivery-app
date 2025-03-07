package com.example.deliveryapp.common.exception.custom_exception;

import com.example.deliveryapp.common.exception.errorcode.ErrorCode;

public class ServerException extends RuntimeException{
    private ErrorCode errorCode;
    public ServerException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 부모 클래스의 메시지 설정!
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return this.errorCode;
    }
}
