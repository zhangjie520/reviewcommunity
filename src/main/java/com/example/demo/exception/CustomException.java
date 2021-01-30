package com.example.demo.exception;

/**
 * @Author: codeape
 * @Date: 2021/1/19 21:42
 * @Version: 1.0
 */
public class CustomException extends RuntimeException {
    private String message;
    private Integer code;
    public CustomException(ICustomErrorCode customErrorCode) {
        this.message=customErrorCode.getMessage();
        this.code=customErrorCode.getCode();
    }

    @Override
    public String getMessage() {
        return this.message;
    }
    public Integer getCode(){
        return this.code;
    }
}
