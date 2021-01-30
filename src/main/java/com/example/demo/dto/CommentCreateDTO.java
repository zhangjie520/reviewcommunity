package com.example.demo.dto;

import lombok.Data;

/**
 * @Author: codeape
 * @Date: 2021/1/20 16:52
 * @Version: 1.0
 */
@Data
public class CommentCreateDTO {
    private Integer parentId;
    private String content;
    private Integer type;
}
