package com.example.demo.advice;

import com.alibaba.fastjson.JSON;
import com.example.demo.dto.ResultDTO;
import com.example.demo.exception.CustomErrorCode;
import com.example.demo.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: codeape
 * @Date: 2021/1/19 21:23
 * @Version: 1.0
 */
@ControllerAdvice
public class CustomExceptionAdvice {

    @ExceptionHandler(Exception.class)
    ModelAndView handleControllerException(Model model, Throwable e, HttpServletRequest request,
                                           HttpServletResponse response) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)){
            //if json is the request type
                ResultDTO resultDTO=null;
                if (e instanceof CustomException){
                    resultDTO=ResultDTO.errorOf((CustomException) e);
                }else {
                    resultDTO= ResultDTO.errorOf(CustomErrorCode.SYSTEM_ERROR);
                }
                try {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.setStatus(200);
                    PrintWriter writer = response.getWriter();
                    writer.write(JSON.toJSONString(resultDTO));
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return null;
        } else {
            if (e instanceof CustomException){
                model.addAttribute("message",e.getMessage());
            }else {
                model.addAttribute("message","服务器冒烟了，稍后再试");
            }
            return new ModelAndView("error");
        }
    }

//    private HttpStatus getStatus(HttpServletRequest request) {
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        if (statusCode == null) {
//            return HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return HttpStatus.valueOf(statusCode);
//    }
}
