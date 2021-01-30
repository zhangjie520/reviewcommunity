package com.example.demo.exception;

/**
 * @Author: codeape
 * @Date: 2021/1/19 21:44
 * @Version: 1.0
 */
public enum  CustomErrorCode implements ICustomErrorCode {
    QUESTION_NOT_FOUNT(2000,"question not found"),
    USER_NOT_FOUNT(2001,"user not found,please login"),
    TARGET_PARAM_NOT_FOUND(2002,"comment target is not  found"),
    SYSTEM_ERROR(2003,"system error"),
    TYPE_PARAM_ERROR(2004,"type error"),
    COMMENT_NOT_FOUND(2005,"comment is not exist"),
    CONTENT_IS_EMPTY(2006,"content can not be empty"),
    NOTIFICATION_NOT_FOUND(2007,"notification not found"),
    NOTIFICATION_NOT_MATCH(2008,"the notification is not your"),
    FILE_UPLOAD_FILE(2009,"file upload file,please upload again"),
    ;
    private Integer code;
    private String message;
    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    CustomErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
