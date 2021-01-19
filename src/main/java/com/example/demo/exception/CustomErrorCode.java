package com.example.demo.exception;

/**
 * @Author: codeape
 * @Date: 2021/1/19 21:44
 * @Version: 1.0
 */
public enum  CustomErrorCode implements ICustomErrorCode {
    QUESTION_NOT_FOUNT("question not found");

    @Override
    public String getMessage() {
        return this.message;
    }
    private String message;

    CustomErrorCode(String message) {
        this.message = message;
    }
}
