package com.tree.community.controller;

import com.tree.community.dto.NotificationDTO;
import com.tree.community.dto.ResultDTO;
import com.tree.community.enums.NotificationTypeEnum;
import com.tree.community.model.User;
import com.tree.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{id}")
    public String profile(@PathVariable(name = "id")Long id,
                          HttpServletRequest request, Model model){


        User user = (User) request.getSession().getAttribute("user");

        NotificationDTO notificationDTO = notificationService.read(id,user);

        if(NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()
            || NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()){
            return "redirect:/question/" + notificationDTO.getOuterid();
        }else{
            return "redirect:/";
        }

    }

    @ResponseBody
    @RequestMapping(value = "/deleteMsgs",method = RequestMethod.POST)
    public Object deleteMsgs(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        notificationService.deleteMsgs(user.getId());
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/readMsgs",method = RequestMethod.POST)
    public Object readMsgs(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        notificationService.readMsgs(user.getId());
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/deleteMsg",method = RequestMethod.POST)
    public Object deleteMsg(@RequestBody Map<String,Long> map,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        notificationService.deleteMsg(map.get("notificationId"),user.getId());
        return ResultDTO.okOf();
    }
}
