package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.Data;

/**
 * @Author: codeape
 * @Date: 2021/1/16 21:37
 * @Version: 1.0
 */
@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private User user;
}
