package com.tree.community.controller;

import com.tree.community.dto.AccessTokenDTO;
import com.tree.community.dto.OauthsUser;
import com.tree.community.dto.UserDTO;
import com.tree.community.mapper.UserMapper;
import com.tree.community.model.User;
import com.tree.community.model.UserExample;
import com.tree.community.model.Useroauths;
import com.tree.community.provider.GithubProvider;
import com.tree.community.provider.QQProvider;
import com.tree.community.service.UseroauthsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private QQProvider qqProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirecturi;

    @Value("${qq.client.id}")
    private String clientIdqq;
    @Value("${qq.client.secret}")
    private String clientSecretqq;
    @Value("${qq.redirect.uri}")
    private String redirecturiqq;

    @Autowired
    private UseroauthsService useroauthsService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/loginCallback")
    public String callback(@RequestParam(name = "code")String code,
                                 @RequestParam(name = "state")String state,
                                 HttpServletRequest request, RedirectAttributes model,HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirecturi);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        OauthsUser oauthsUser = githubProvider.getUser(accessToken);
        if(oauthsUser !=null && oauthsUser.getId()!=null){
            Object githubBind = request.getSession().getAttribute("githubBind");
            if(githubBind != null){
                User user = (User) request.getSession().getAttribute("user");
                UserDTO dto = new UserDTO();
                dto.setOauthsId(oauthsUser.getId());
                dto.setOauthsType(String.valueOf(0));
                int result = useroauthsService.oauthsBind(user.getId(), dto);
                request.getSession().removeAttribute("githubBind");
                if(result == -1){
                    model.addFlashAttribute("bindResult","-10");
                }else if(result == -2){
                    model.addFlashAttribute("bindResult","-2");
                }
                return "redirect:/user/set/account#bind";
            }
            User user = useroauthsService.findByAccountId(oauthsUser.getId(),0);
            if(user != null){
                HttpSession session = request.getSession();
                session.setAttribute("user",user);
                //设置第三方登录一个月记住我
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                UserExample example = new UserExample();
                example.createCriteria()
                        .andPhoneEqualTo(user.getPhone());
                userMapper.updateByExampleSelective(user, example);
                Cookie token1 = new Cookie("token", token);
                token1.setMaxAge(30*24*60*60);
                token1.setPath("/");
                response.addCookie(token1);
                return "redirect:/";
            }
            Useroauths useroauths = new Useroauths();
            useroauths.setAccountId(String.valueOf(oauthsUser.getId()));
            useroauths.setType(0);
            model.addFlashAttribute("oauthsInfo","为了安全，请完善信息并绑定手机号");
            model.addFlashAttribute("useroauths",useroauths);
            return "redirect:/register";
        }else{
            log.error("callback get github error,{}", oauthsUser);
            //登录失败，重新登录
            return "redirect:/";
        }
    }

    @GetMapping("/loginCallbackqq")
    public String loginCallbackqq(@RequestParam(name = "code")String code,
                           HttpServletRequest request, RedirectAttributes model,HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientIdqq);
        accessTokenDTO.setClient_secret(clientSecretqq);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirecturiqq);
        String accessToken = qqProvider.getAccessToken(accessTokenDTO);
        OauthsUser oauthsUser = qqProvider.getUser(accessToken);
        if(oauthsUser !=null && oauthsUser.getOpenid()!=null){
            Object githubBind = request.getSession().getAttribute("qqBind");
            if(githubBind != null){
                User user = (User) request.getSession().getAttribute("user");
                UserDTO dto = new UserDTO();
                dto.setOauthsId(oauthsUser.getOpenid());
                dto.setOauthsType(String.valueOf(1));
                int result = useroauthsService.oauthsBind(user.getId(), dto);
                request.getSession().removeAttribute("qqBind");
                if(result == -1){
                    model.addFlashAttribute("bindResult","-11");
                }else if(result == -2){
                    model.addFlashAttribute("bindResult","-2");
                }
                return "redirect:/user/set/account#bind";
            }
            User user = useroauthsService.findByAccountId(oauthsUser.getOpenid(),1);
            if(user != null){
                HttpSession session = request.getSession();
                session.setAttribute("user",user);
                //设置第三方登录一个月记住我
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                UserExample example = new UserExample();
                example.createCriteria()
                        .andPhoneEqualTo(user.getPhone());
                userMapper.updateByExampleSelective(user, example);
                Cookie token1 = new Cookie("token", token);
                token1.setMaxAge(30*24*60*60);
                token1.setPath("/");
                response.addCookie(token1);
                return "redirect:/";
            }
            Useroauths useroauths = new Useroauths();
            useroauths.setAccountId(String.valueOf(oauthsUser.getOpenid()));
            useroauths.setType(1);
            model.addFlashAttribute("oauthsInfo","为了安全，请完善信息并绑定手机号");
            model.addFlashAttribute("useroauths",useroauths);
            return "redirect:/register";
        }else{
            log.error("callback get qq error,{}", oauthsUser);
            //登录失败，重新登录
            return "redirect:/";
        }
    }

}
