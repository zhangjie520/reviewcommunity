package com.example.demo.dto;

import com.example.demo.model.Question;
import com.example.demo.model.User;
import lombok.Data;

/**
 * @Author: codeape
 * @Date: 2021/1/26 22:28
 * @Version: 1.0
 */
@Data
public class NotificationDTO {
    private Integer id;
    private Integer notifier;
    private Integer receiver;
    private Integer outerId;//当是回复question时是question的Id，当回复评论时是评论对应question的id
    private Long gmtCreate;
    private Integer type;
    private Integer status;
    private String typeValue;
    private User notifierUser;
    private Question question;
}
