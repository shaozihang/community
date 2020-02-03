package com.tree.community.controller;

import com.tree.community.dto.NotificationDTO;
import com.tree.community.enums.NotificationTypeEnum;
import com.tree.community.model.User;
import com.tree.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id")Long id,
                          HttpServletRequest request, Model model){


        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id,user);

        if(NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()
            || NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()){
            return "redirect:/question/" + notificationDTO.getOuterid();
        }else{
            return "redirect:/";
        }

    }
}
