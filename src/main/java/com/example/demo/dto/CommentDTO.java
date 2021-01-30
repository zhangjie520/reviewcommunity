package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.Data;

/**
 * @Author: codeape
 * @Date: 2021/1/21 17:59
 * @Version: 1.0
 */
@Data
public class CommentDTO {
    private Integer id;
    private Integer commentator;
    private Integer parentId;
    private Integer type;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Long commentCount;
    private User user;
}
