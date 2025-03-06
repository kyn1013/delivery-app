package com.example.deliveryapp.common.exception.custom_exception;

import com.example.deliveryapp.common.exception.errorcode.ErrorCode;

public class InvalidRequestException extends RuntimeException{
    private ErrorCode errorCode;
    public InvalidRequestException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return this.errorCode;
    }
}
