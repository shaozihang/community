package com.tree.community.controller;

import com.tree.community.dto.ResultDTO;
import com.tree.community.model.User;
import com.tree.community.model.UserLike;
import com.tree.community.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserLikeController {

    @Autowired
    private RedisService redisService;

    @ResponseBody
    @RequestMapping(value = "/like",method = RequestMethod.POST)
    public Object post(@RequestBody UserLike userLike,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(2000,"未登录不能进行点赞，请先登录");
        }

        if(userLike.getStatus() == 0){
            redisService.saveLikedRedis(String.valueOf(userLike.getQuestionId()),String.valueOf(userLike.getLikedUserId()),String.valueOf(user.getId()),userLike.getType());
            redisService.incrementLikedCount(String.valueOf(userLike.getLikedUserId()),userLike.getType());
        }else if(userLike.getStatus() == 1){
            redisService.unlikeFromRedis(String.valueOf(userLike.getQuestionId()),String.valueOf(userLike.getLikedUserId()),String.valueOf(user.getId()),userLike.getType());
            redisService.decrementLikedCount(String.valueOf(userLike.getLikedUserId()),userLike.getType());
        }
        return ResultDTO.okOf();
    }
}
