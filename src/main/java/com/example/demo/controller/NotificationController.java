package com.example.demo.controller;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.enums.NotificationTypeEnum;
import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: codeape
 * @Date: 2021/1/27 22:28
 * @Version: 1.0
 */
@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String notification(@PathVariable("id") Integer notificationId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(notificationId, user);
        if (notificationDTO.getType() == NotificationTypeEnum.COMMENT.getType() ||
                notificationDTO.getType() == NotificationTypeEnum.QUESTION.getType()) {
            return "redirect:/question/" + notificationDTO.getOuterId();
        }
        return "/";
    }
}
