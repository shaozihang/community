package com.tree.community.controller;

import com.tree.community.dto.ResultDTO;
import com.tree.community.model.User;
import com.tree.community.model.Useroauths;
import com.tree.community.service.UseroauthsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UseroauthsController {

    @Autowired
    private UseroauthsService useroauthsService;

    @ResponseBody
    @RequestMapping(value = "/bind",method = RequestMethod.POST)
    public Object updateUserInfo(@RequestBody Useroauths useroauths, HttpServletRequest request){
        if(useroauths.getType() == 0){
            request.getSession().setAttribute("githubBind",1);
        }else if(useroauths.getType() == 1){
            request.getSession().setAttribute("qqBind",1);
        }
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/Unbind",method = RequestMethod.POST)
    public Object Unbind(@RequestBody Useroauths useroauths, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(useroauths.getType() == 0){
            useroauthsService.unbind(user.getId(),0);
        }else if(useroauths.getType() == 1){
            useroauthsService.unbind(user.getId(),1);
        }
        return ResultDTO.okOf();
    }
}
