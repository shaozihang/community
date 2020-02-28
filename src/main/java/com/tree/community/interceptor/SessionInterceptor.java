package com.tree.community.interceptor;

import com.tree.community.mapper.UserMapper;
import com.tree.community.model.User;
import com.tree.community.model.UserExample;
import com.tree.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = false;//如果等于true表示是记住我状态登录的
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            Cookie[] cookies = request.getCookies();
            if(cookies!=null&&cookies.length!=0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")) {
                        String token = cookie.getValue();
                        UserExample userExample = new UserExample();
                        userExample.createCriteria()
                                .andTokenEqualTo(token);
                        List<User> users = userMapper.selectByExample(userExample);
                        if (users.size() != 0) {
                            flag = true;
                            request.getSession().setAttribute("user", users.get(0));
                            Long unreadCount = notificationService.unreadCount(users.get(0).getId());
                            request.getSession().setAttribute("unreadCount",unreadCount);
                        }
                        break;
                    }
                }
            }
        }
        if(!flag){
            if(user != null){
                Long unreadCount = notificationService.unreadCount(user.getId());
                request.getSession().setAttribute("unreadCount",unreadCount);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
