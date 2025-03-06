package com.example.deliveryapp.common.exception.custom_exception;

import com.example.deliveryapp.common.exception.errorcode.ErrorCode;

public class ServerException extends RuntimeException{
    private ErrorCode errorCode;
    public ServerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return this.errorCode;
    }
}
