package com.example.demo.dto;

import lombok.Data;

/**
 * @Author: codeape
 * @Date: 2021/1/29 22:47
 * @Version: 1.0
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
}
