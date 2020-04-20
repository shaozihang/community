package com.tree.community.controller;

import com.tree.community.dto.ResultDTO;
import com.tree.community.dto.UserDTO;
import com.tree.community.mapper.UserMapper;
import com.tree.community.model.User;
import com.tree.community.model.UserExample;
import com.tree.community.service.UserService;
import com.tree.community.service.UseroauthsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
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
        }else if(type == 2 || type == 4 || type == 6){//4是修改手机号的,6是换绑邮箱的
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
            if(type == 4 || type == 6){
                session.removeAttribute("userCode"+type);
                session.removeAttribute("userPhone"+type);
                if(type == 4){
                    session.setAttribute("updatePhonePhone",userDTO.getPhone());
                }else if(type == 6){
                    session.setAttribute("bindEmailPhone",userDTO.getPhone());
                }
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
            if(type == 1){
                if(userDTO.getAccount().contains("@")){
                    example.createCriteria()
                            .andEmailEqualTo(userDTO.getAccount());
                }else{
                    example.createCriteria()
                            .andPhoneEqualTo(userDTO.getAccount());
                }
            }else{
                example.createCriteria()
                        .andPhoneEqualTo(userDTO.getPhone());
            }
            userMapper.updateByExampleSelective(user, example);
            Cookie token1 = new Cookie("token", token);
            token1.setMaxAge(30*24*60*60);
            token1.setPath("/");
            response.addCookie(token1);
        }
        return result;
    }

    @RequestMapping(value = "/checkAccount",method = RequestMethod.POST)
    @ResponseBody
    public Object checkAccount(HttpServletRequest request,
                            @RequestBody Map<String,String> map){
        String account = map.get("account");
        List<User> users = userService.checkAccount(account);
        if(users.size() == 0){
            return ResultDTO.errorOf(2023,"用户不存在");
        }
        return ResultDTO.okOf();
    }

    @RequestMapping(value = "/modifyPwd",method = RequestMethod.POST)
    @ResponseBody
    public Object modifyPwd(HttpServletRequest request,
                            @RequestBody Map<String,String> map){
        HttpSession session = request.getSession();
        if(map.get("modifyAccount").contains("@")){
            Object sendEmailTime = session.getAttribute("sendEmailTime");
            if(sendEmailTime == null){
                return ResultDTO.errorOf(2025,"请发送验证码！");
            }
            Object sendEmail = session.getAttribute("sendEmail");
            Object sendEmailCode = session.getAttribute("sendEmailCode");
            Date date = new Date();
            if(date.getTime() - Long.valueOf(String.valueOf(sendEmailTime)) > 1000*60*5){
                session.removeAttribute("sendEmailTime");
                return ResultDTO.errorOf(2026,"验证码已过期！");
            }else if(!map.get("code").equals(String.valueOf(sendEmailCode))){
                return ResultDTO.errorOf(2027,"验证码错误！");
            }else if(!map.get("modifyAccount").equals(String.valueOf(sendEmail))){
                return ResultDTO.errorOf(2028,"邮箱与验证码不匹配");
            }else{
                userService.modifyPwd(map);
            }
            session.removeAttribute("sendEmailTime");
            session.removeAttribute("sendEmail");
            session.removeAttribute("sendEmailCode");
        }else{
            Object userCode = session.getAttribute("userCode3");
            Object userPhone = session.getAttribute("userPhone3");
            if(userCode == null){
                return ResultDTO.errorOf(2011,"验证码已过期，请重新发送");
            }
            if(!map.get("code").equals(String.valueOf(userCode))){
                return ResultDTO.errorOf(2012,"验证码错误");
            }
            if(!map.get("modifyAccount").equals(String.valueOf(userPhone))){
                return ResultDTO.errorOf(2013,"手机号与验证码不匹配");
            }
            userService.modifyPwd(map);
            session.removeAttribute("userCode3");
            session.removeAttribute("userPhone3");
        }
        return ResultDTO.okOf();
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        String updatePhonePhone = (String) request.getSession().getAttribute("updatePhonePhone");
        Object bindEmailPhone = request.getSession().getAttribute("bindEmailPhone");
        if(updatePhonePhone != null){
            request.getSession().removeAttribute("updatePhonePhone");
        }
        if(bindEmailPhone != null){
            request.getSession().removeAttribute("bindEmailPhone");
        }
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
