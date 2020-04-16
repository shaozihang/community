package com.tree.community.interceptor;

import com.tree.community.mapper.UserMapper;
import com.tree.community.model.User;
import com.tree.community.model.UserExample;
import com.tree.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

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
        if (handler instanceof ResourceHttpRequestHandler)
            return true;
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
                            request.getSession().setAttribute("user", users.get(0));
                            Long unreadCount = notificationService.unreadCount(users.get(0).getId());
                            request.getSession().setAttribute("unreadCount",unreadCount);
                        }
                        break;
                    }
                }
            }
        }else{
            User user1 = userMapper.selectByPrimaryKey(user.getId());
            request.getSession().setAttribute("user", user1);
            Long unreadCount = notificationService.unreadCount(user1.getId());
            request.getSession().setAttribute("unreadCount",unreadCount);
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
