package com.tree.community.controller;

import com.tree.community.dto.BookMarkDTO;
import com.tree.community.dto.PaginationDTO;
import com.tree.community.dto.ResultDTO;
import com.tree.community.model.FollowAndFans;
import com.tree.community.model.Question;
import com.tree.community.model.User;
import com.tree.community.service.*;
import com.tree.community.util.ScoreToGradeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class FollowAndFansController {

    @Autowired
    private FollowAndFansService followAndFansService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private BookMarkService bookMarkService;

    @Autowired
    private CollectionService collectionService;

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
                          @RequestParam(name = "size",defaultValue = "10")Integer size){
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
        userInfo.setGrade(ScoreToGradeUtils.scoreToGrade(userInfo.getScore()));
        PaginationDTO userList = null;
        int collectionCount = 0;
        List<BookMarkDTO> publicBookMarkList = bookMarkService.getPublicBookMark(id);
        for (BookMarkDTO bookMarkDTO : publicBookMarkList) {
            collectionCount += bookMarkDTO.getCollectionCount();
        }
        if("follow".equals(action)){
            userList = followAndFansService.getFollowList(id, user, page, size,1);
            model.addAttribute("section","follow");
        }else if("fans".equals(action)){
            userList = followAndFansService.getFollowList(id, user, page, size, 2);
            model.addAttribute("section","fans");
        }else if("bookMark".equals(action)){
            model.addAttribute("section","bookMark");
        }

        int quCount = questionService.getQuCountById(id);
        model.addAttribute("publicBookMarkList",publicBookMarkList);
        model.addAttribute("userList",userList);
        model.addAttribute("userInfo",userInfo);
        model.addAttribute("followStatus",followStatus);
        model.addAttribute("followOtherStatus",followOtherStatus);
        model.addAttribute("quCount",quCount);
        model.addAttribute("collectionCount",collectionCount);
        return "user";
    }

    @GetMapping("/user/bookMarks/{id}")
    public String userCollections(@PathVariable(name = "id")Long id,
                                  @RequestParam(name = "id")Long userId,
                                  HttpServletRequest request, Model model,
                                  @RequestParam(name = "page",defaultValue = "1")Integer page,
                                  @RequestParam(name = "size",defaultValue = "5")Integer size) {

        User user = (User) request.getSession().getAttribute("user");
        int followStatus;
        int followOtherStatus = 0;
        if(user == null){
            followStatus = 0;
        }else{
            followStatus = followAndFansService.getFollowStatus(user.getId(), userId);
            if(followStatus == -1){
                followStatus = 0;
                followOtherStatus = -1;
            }
        }
        int collectionCount = 0;
        List<BookMarkDTO> publicBookMarkList = bookMarkService.getPublicBookMark(userId);
        for (BookMarkDTO bookMarkDTO : publicBookMarkList) {
            collectionCount += bookMarkDTO.getCollectionCount();
        }
        User userInfo = userService.getUserInfoById(userId);
        userInfo.setGrade(ScoreToGradeUtils.scoreToGrade(userInfo.getScore()));
        BookMarkDTO bookMarkDTO = bookMarkService.getBookMarkById(id);
        List<Question> questions = collectionService.getCollectionByFolderId(id);
        int quCount = questionService.getQuCountById(userId);
        model.addAttribute("publicBookMarkList",publicBookMarkList);
        model.addAttribute("bookMark",bookMarkDTO);
        model.addAttribute("questions",questions);
        model.addAttribute("section","collection");
        model.addAttribute("userInfo",userInfo);
        model.addAttribute("followStatus",followStatus);
        model.addAttribute("followOtherStatus",followOtherStatus);
        model.addAttribute("quCount",quCount);
        model.addAttribute("collectionCount",collectionCount);
        return "user";
    }
}
