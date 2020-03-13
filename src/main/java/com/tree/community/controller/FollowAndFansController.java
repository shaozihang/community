package com.tree.community.controller;

import com.tree.community.dto.PaginationDTO;
import com.tree.community.dto.ResultDTO;
import com.tree.community.model.FollowAndFans;
import com.tree.community.model.User;
import com.tree.community.service.FollowAndFansService;
import com.tree.community.service.QuestionService;
import com.tree.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FollowAndFansController {

    @Autowired
    private FollowAndFansService followAndFansService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @ResponseBody
    @RequestMapping(value = "/fans",method = RequestMethod.POST)
    public Object fans(@RequestBody FollowAndFans followAndFans,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(2000,"未登录不能进行操作，请先登录");
        }
        if(user.getId() == followAndFans.getToUserId()){
            return ResultDTO.errorOf(2001,"不能关注自己哦");
        }
        followAndFans.setFromUserId(user.getId());
        followAndFansService.addOrCancelFollow(followAndFans);

        return ResultDTO.okOf();
    }

    @GetMapping("/users/{action}")
    public String profile(@PathVariable(name = "action")String action,
                          @RequestParam(name = "id")Long id,
                          HttpServletRequest request, Model model,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5")Integer size){
        User user = (User) request.getSession().getAttribute("user");
        int followStatus;
        int followOtherStatus = 0;
        if(user == null){
            followStatus = 0;
        }else{
            followStatus = followAndFansService.getFollowStatus(user.getId(), id);
            if(followStatus == -1){
                followStatus = 0;
                followOtherStatus = -1;
            }
        }
        User userInfo = userService.getUserInfoById(id);
        PaginationDTO userList;
        if("follow".equals(action)){
            userList = followAndFansService.getFollowList(id, user, page, size,1);
            model.addAttribute("section","follow");
        }else {
            userList = followAndFansService.getFollowList(id, user, page, size, 2);
            model.addAttribute("section","fans");
        }

        int quCount = questionService.getQuCountById(id);
        model.addAttribute("userList",userList);
        model.addAttribute("userInfo",userInfo);
        model.addAttribute("followStatus",followStatus);
        model.addAttribute("followOtherStatus",followOtherStatus);
        model.addAttribute("quCount",quCount);
        return "user";
    }
}
