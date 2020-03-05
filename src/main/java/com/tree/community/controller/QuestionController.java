package com.tree.community.controller;

import com.tree.community.dto.CommentDTO;
import com.tree.community.dto.QuestionDTO;
import com.tree.community.enums.CommentTypeEnum;
import com.tree.community.model.Question;
import com.tree.community.model.User;
import com.tree.community.service.CommentService;
import com.tree.community.service.QuestionService;
import com.tree.community.service.RedisService;
import com.tree.community.service.UserLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Long id, Model model, HttpServletRequest request){
        QuestionDTO questionDTO = questionService.getById(id);
        List<Question> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION,request);
        User user = (User) request.getSession().getAttribute("user");
        if(user != null){
            Integer status = userLikeService.selectlikeStatus(id, user.getId(), 1);
            questionDTO.setLikeStatus(status);
        }else {
            questionDTO.setLikeStatus(0);
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
        return "question";
    }
}
