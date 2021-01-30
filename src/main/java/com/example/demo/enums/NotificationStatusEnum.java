package com.example.demo.enums;

/**
 * @Author: codeape
 * @Date: 2021/1/26 21:44
 * @Version: 1.0
 */
public enum NotificationStatusEnum {
    UNREAD(1),
    READ(2);

    public Integer getStatus() {
        return status;
    }

    NotificationStatusEnum(Integer status) {

        this.status = status;
    }

    private Integer status;
}
