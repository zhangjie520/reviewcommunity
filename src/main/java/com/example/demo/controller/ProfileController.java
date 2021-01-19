package com.example.demo.controller;

import com.example.demo.dto.PaginationDTO;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: codeape
 * @Date: 2021/1/18 15:07
 * @Version: 1.0
 */
@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/profile/{action}")
    public String profile(@RequestParam(value = "page",defaultValue = "1") Integer pageIndex,
                          @RequestParam(value = "size",defaultValue = "5") Integer size,
                          @PathVariable("action") String action, Model model,
                          HttpServletRequest request){
        User user= (User) request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }

        if (action.equals("questions")){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");
            PaginationDTO<QuestionDTO> paginationDTO=questionService.listByUserId(user.getId(),pageIndex,size);
            model.addAttribute("pagination",paginationDTO);
        }else if (action.equals("replies")){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        return "profile";
    }
}
