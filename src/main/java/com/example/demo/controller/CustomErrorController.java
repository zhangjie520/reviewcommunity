package com.example.demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: codeape
 * @Date: 2021/1/19 21:57
 * @Version: 1.0
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return null;
    }
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response, Model model) {
        HttpStatus status = getStatus(request);
        if (status.is4xxClientError()){
            model.addAttribute("message","you request is not correct ,please check it!");
        }else {
            model.addAttribute("message","server is encountering anonymous error,please request later!");
        }
        return new ModelAndView("error");
    }
    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        }
        catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
