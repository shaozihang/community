package com.tree.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SuccessControler {

    @GetMapping("/success")
    public String success(@RequestParam("info")String info, Model model){
        model.addAttribute("success",info);
        return "success";
    }
}
