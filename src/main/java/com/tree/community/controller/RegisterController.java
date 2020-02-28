package com.tree.community.controller;

import com.aliyuncs.exceptions.ClientException;
import com.tree.community.dto.ResultDTO;
import com.tree.community.dto.UserDTO;
import com.tree.community.mapper.UserMapper;
import com.tree.community.model.User;
import com.tree.community.model.UserExample;
import com.tree.community.model.Useroauths;
import com.tree.community.provider.AliyunProvider;
import com.tree.community.service.UserService;
import com.tree.community.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class RegisterController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register(@ModelAttribute("oauthsInfo")String oauthsInfo,
                           @ModelAttribute("useroauths") Useroauths useroauths, Model model){
        model.addAttribute("oauthsInfo",oauthsInfo);
        model.addAttribute("useroauths",useroauths);
        return "register";
    }


    @RequestMapping(value = "/registerUser",method = RequestMethod.POST)
    @ResponseBody
    public Object registerUser(HttpServletRequest request,
                               @RequestBody UserDTO userDTO){
        HttpSession session = request.getSession();
        Object userCode = session.getAttribute("userCode1");
        Object userPhone = session.getAttribute("userPhone1");
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNickNameEqualTo(userDTO.getNickName());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() != 0){
            return ResultDTO.errorOf(2010,"昵称已存在");
        }
        if(userCode == null){
            return ResultDTO.errorOf(2011,"验证码已过期，请重新发送");
        }
        if(!userDTO.getCode().equals(String.valueOf(userCode))){
            return ResultDTO.errorOf(2012,"验证码错误");
        }
        if(!userDTO.getPhone().equals(String.valueOf(userPhone))){
            return ResultDTO.errorOf(2013,"手机号与验证码不匹配");
        }
        ResultDTO result = userService.createOrBind(userDTO);
        session.removeAttribute("userCode1");
        session.removeAttribute("userPhone1");
        return result;
    }

    @RequestMapping(value = "/getCode/{type}",method = RequestMethod.POST)
    @ResponseBody
    public Object getCode(@RequestBody String phone,@PathVariable(name = "type")Integer type, HttpServletRequest request) throws ClientException {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andPhoneEqualTo(phone);
        List<User> users = userMapper.selectByExample(userExample);
        if(type == 1){
            if(users.size() != 0){
                return ResultDTO.errorOf(2008,"手机号已存在");
            }
        }
        if(type == 2 || type == 3){
            if(users.size() == 0){
                return ResultDTO.errorOf(2014,"该手机号未注册");
            }
        }
        AliyunProvider aliyunProvider = new AliyunProvider();
        String code = aliyunProvider.SendSms(phone,type,request);
        if(!code.equals("OK")){
            return ResultDTO.errorOf(2009,"验证码发送失败，请重新发送");
        }
        return ResultDTO.okOf();
    }

}
