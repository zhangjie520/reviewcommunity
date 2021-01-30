package com.example.demo.enums;

/**
 * @Author: codeape
 * @Date: 2021/1/20 22:03
 * @Version: 1.0
 */
public enum  CommentTypeEnum {

    QUESTION(1),
    COMMENT(2),
    ;
    private Integer type;

    CommentTypeEnum(Integer type) {
        this.type=type;
    }

    public Integer getType() {
        return type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType()==type){
                return true;
            }
        }
        return false;
    }
}
