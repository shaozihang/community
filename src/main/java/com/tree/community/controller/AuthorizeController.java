package com.tree.community.controller;

import com.tree.community.dto.AccessTokenDTO;
import com.tree.community.dto.GithubUser;
import com.tree.community.mapper.UserMapper;
import com.tree.community.model.User;
import com.tree.community.model.UserExample;
import com.tree.community.model.Useroauths;
import com.tree.community.provider.GithubProvider;
import com.tree.community.service.UserService;
import com.tree.community.service.UseroauthsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
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

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSsecret;
    @Value("${github.redirect.uri}")
    private String redirecturi;

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
        accessTokenDTO.setClient_secret(clientSsecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirecturi);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null && githubUser.getId()!=null){
            User user = useroauthsService.findByAccountId(String.valueOf(githubUser.getId()));
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
            useroauths.setAccountId(String.valueOf(githubUser.getId()));
            useroauths.setType(0);
            model.addFlashAttribute("oauthsInfo","为了安全，请完善信息并绑定手机号");
            model.addFlashAttribute("useroauths",useroauths);
            return "redirect:/register";
        }else{
            log.error("callback get github error,{}",githubUser);
            //登录失败，重新登录
            return "redirect:/";
        }
    }

}
