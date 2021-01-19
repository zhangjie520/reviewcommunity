package com.example.demo.exception;

/**
 * @Author: codeape
 * @Date: 2021/1/19 21:42
 * @Version: 1.0
 */
public class CustomException extends RuntimeException {
    private String message;
    public CustomException(ICustomErrorCode customErrorCode) {
        this.message=customErrorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
