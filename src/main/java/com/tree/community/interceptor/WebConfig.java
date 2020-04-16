package com.tree.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private adminLoginInterceptor adminLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/notification/**","/publish/**","/file/**","/user/profile/{action}","/user/bookMark/{id}",
                "/user/set/{action}","/question/**","/comment/**","/bookMark/**","/collection/**").excludePathPatterns("/question/{id}","/comment",
                "/comment/{id}", "/bookMark/getBookMark");
        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/admin/**");
    }

}
