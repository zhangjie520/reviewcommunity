package com.example.demo.dto;

import com.example.demo.exception.CustomErrorCode;
import com.example.demo.exception.CustomException;
import lombok.Data;

import java.util.List;

/**
 * @Author: codeape
 * @Date: 2021/1/20 21:13
 * @Version: 1.0
 */
@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public static Object okof() {
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("评论成功");
        return resultDTO;
    }

    public static ResultDTO errorOf(Integer code,String message) {
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomErrorCode errorCode) {
        return ResultDTO.errorOf(errorCode.getCode(),errorCode.getMessage());
    }

    public static ResultDTO errorOf(CustomException e) {
        return ResultDTO.errorOf(e.getCode(),e.getMessage());
    }

    public static <T> ResultDTO okof(T t) {
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setMessage("评论成功");
        resultDTO.setCode(200);
        resultDTO.setData(t);
        return resultDTO;
    }
}
