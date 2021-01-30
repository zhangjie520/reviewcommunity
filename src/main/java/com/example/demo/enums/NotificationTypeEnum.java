package com.example.demo.enums;

/**
 * @Author: codeape
 * @Date: 2021/1/26 21:29
 * @Version: 1.0
 */
public enum  NotificationTypeEnum {
    COMMENT(1,"回复了评论"),
    QUESTION(2,"回复了问题");

    private Integer type;
    private String data;

    public Integer getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    /**
     * 返回type对应描述，展示给前端
     * @param type
     * @return
     */

    public static String nameOfType(Integer type) {
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType()==type){
                return notificationTypeEnum.getData();
            }
        }
        return "";
    }

    NotificationTypeEnum(Integer type, String data) {
        this.type = type;
        this.data = data;
    }
}
