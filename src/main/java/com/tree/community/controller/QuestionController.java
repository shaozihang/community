package com.tree.community.controller;

import com.tree.community.dto.CommentDTO;
import com.tree.community.dto.QuestionDTO;
import com.tree.community.dto.ResultDTO;
import com.tree.community.enums.CommentTypeEnum;
import com.tree.community.model.Question;
import com.tree.community.model.User;
import com.tree.community.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserLikeService userLikeService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CollectionService collectionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Long id, Model model, HttpServletRequest request){
        QuestionDTO questionDTO = questionService.getById(id);
        List<Question> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION,request);
        User user = (User) request.getSession().getAttribute("user");
        int collectionStatus;
        if(user != null){
            Integer status = userLikeService.selectlikeStatus(id,id, user.getId(), 1);
            questionDTO.setLikeStatus(status);
            collectionStatus = collectionService.getCollectionStatus(id, user.getId());
        }else {
            questionDTO.setLikeStatus(0);
            collectionStatus = 0;
        }
        Integer likeCount = redisService.selectlikeCount(id, 1);
        if(likeCount != null){
            questionDTO.setLikeCount(questionDTO.getLikeCount()+likeCount);
        }

        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        model.addAttribute("collectionStatus",collectionStatus);
        return "question";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteQu",method = RequestMethod.POST)
    public Object deleteQu(@RequestBody Map<String,Long> map, HttpServletRequest request){
        questionService.deleteQu(map.get("questionId"),request);
        return ResultDTO.okOf();
    }
}
