package com.tree.community.controller.admin;

import com.tree.community.model.User;
import com.tree.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminLoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/adminLogin")
    public String adminLogin(){
        return "adminLogin";
    }

    @RequestMapping(value = "/adminLogin",method = RequestMethod.POST)
    @ResponseBody
    public Object adminLogin(@RequestBody User user, HttpServletRequest request){
        Object result = userService.adminLogin(user,request);
        return result;
    }

    @GetMapping("/adminLogout")
    public String adminLogout(HttpServletRequest request){
        request.getSession().removeAttribute("adminUser");
        return "redirect:/adminLogin";
    }
}
