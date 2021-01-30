package com.example.demo.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: codeape
 * @Date: 2021/1/25 21:11
 * @Version: 1.0
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}