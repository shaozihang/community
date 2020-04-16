package com.tree.community.controller;

import com.tree.community.dto.CommentCreateDTO;
import com.tree.community.dto.CommentDTO;
import com.tree.community.dto.ResultDTO;
import com.tree.community.enums.CommentTypeEnum;
import com.tree.community.model.Comment;
import com.tree.community.model.User;
import com.tree.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(2000,"未登录不能进行评论，请先登录");
        }

        if(commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(2001,"输入内容不能为空！");
        }

        Comment comment = new Comment();
        comment.setQuestionId(commentCreateDTO.getQuestionId());
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setTargetId(commentCreateDTO.getTargetId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());

        ResultDTO result = commentService.insert(comment,user,commentCreateDTO.getQuestionId(),request);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id")Long id,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        Long userId;
        int userType;
        if(user == null){
            userId = -1L;
            userType = -1;
        }else{
            userId = user.getId();
            userType = user.getType();
        }
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT,request);
        List<Object> list = new ArrayList<>();
        list.add(commentDTOS);
        list.add(userId);
        list.add(userType);
        return ResultDTO.okOf(list);
    }

    @ResponseBody
    @RequestMapping(value = "/comment/deleteComment",method = RequestMethod.POST)
    public Object deleteComment(@RequestBody Map<String,Long> map){
        commentService.deleteComment(map.get("commentId"),map.get("authorId"));
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/deleteComment2",method = RequestMethod.POST)
    public Object deleteComment2(@RequestBody Map<String,Long> map){
        commentService.deleteComment2(map.get("commentId"),map.get("authorId"),map.get("parentId"));
        return ResultDTO.okOf();
    }

}
