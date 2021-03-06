package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: codeape
 * @Date: 2021/1/14 14:20
 * @Version: 1.0
 */
@Controller
public class HelloController {
    @GetMapping("/hello")
    public String Hello(@RequestParam(name="name") String name,Model model){
        model.addAttribute("name",name);
        return "index";
    }
}
