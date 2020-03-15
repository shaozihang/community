package com.tree.community.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.tree.community.dto.ResultDTO;
import com.tree.community.dto.UserDTO;
import com.tree.community.mapper.UserMapper;
import com.tree.community.mapper.UseroauthsMapper;
import com.tree.community.model.User;
import com.tree.community.model.UserExample;
import com.tree.community.model.Useroauths;
import com.tree.community.service.UserService;
import com.tree.community.service.UseroauthsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UseroauthsService useroauthsService;

    @GetMapping(value = "/login")
    public String login(@RequestParam(value = "oauthsId",required = false)String oauthsId,
                        @RequestParam(value = "oauthsType",required = false)String oauthsType,Model model){
        model.addAttribute("oauthsId",oauthsId);
        model.addAttribute("oauthsType",oauthsType);
        return "login";
    }

    @GetMapping("/findpassword")
    public String findpassword(){
        return "findpassword";
    }

    @RequestMapping(value = "/loginUser/{type}",method = RequestMethod.POST)
    @ResponseBody
    public Object loginUser(HttpServletRequest request, HttpServletResponse response,
                               @RequestBody UserDTO userDTO, @PathVariable(name = "type")Integer type){
        ResultDTO result = null;
        if(type == 1){
            result = userService.loginOrBind(userDTO,request);
        }else if(type == 2 || type == 3){
            HttpSession session = request.getSession();
            Object userCode = session.getAttribute("userCode"+type);
            Object userPhone = session.getAttribute("userPhone"+type);
            if(userCode == null){
                return ResultDTO.errorOf(2011,"验证码已过期，请重新发送");
            }
            if(!userDTO.getCode().equals(String.valueOf(userCode))){
                return ResultDTO.errorOf(2012,"验证码错误");
            }
            if(!userDTO.getPhone().equals(String.valueOf(userPhone))){
                return ResultDTO.errorOf(2013,"手机号与验证码不匹配");
            }
            if(type == 3){
                return ResultDTO.okOf();
            }
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andPhoneEqualTo(userDTO.getPhone());
            List<User> users = userMapper.selectByExample(userExample);
            if(StringUtils.isNotBlank(userDTO.getOauthsId())){
                int typeIsBound = useroauthsService.oauthsBind(users.get(0).getId(), userDTO);
                if(typeIsBound == -1){
                    if(userDTO.getOauthsType().equals("0")){
                        result = ResultDTO.errorOf(2020,"Github账号绑定失败！此账号已绑定过其他的Github账号");
                    }else if(userDTO.getOauthsType().equals("1")){
                        result = ResultDTO.errorOf(2021,"QQ账号绑定失败！此账号已绑定过其他的QQ账号");
                    }
                    session.removeAttribute("userCode"+type);
                    session.removeAttribute("userPhone"+type);
                    return result;
                }
            }
            session.setAttribute("user",users.get(0));
            session.removeAttribute("userCode"+type);
            session.removeAttribute("userPhone"+type);
            result = ResultDTO.okOf();
        }
        //登录成功，写入cookie
        if(userDTO.getFlag() == 1 && result.getCode() == 200){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            UserExample example = new UserExample();
            example.createCriteria()
                    .andPhoneEqualTo(userDTO.getPhone());
            userMapper.updateByExampleSelective(user, example);
            Cookie token1 = new Cookie("token", token);
            token1.setMaxAge(30*24*60*60);
            token1.setPath("/");
            response.addCookie(token1);
        }
        return result;
    }

    @RequestMapping(value = "/modifyPwd",method = RequestMethod.POST)
    @ResponseBody
    public Object modifyPwd(HttpServletRequest request,
                            @RequestBody Map<String,String> map){
        userService.modifyPwd(map);
        HttpSession session = request.getSession();
        session.removeAttribute("userCode3");
        session.removeAttribute("userPhone3");
        return ResultDTO.okOf();
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
