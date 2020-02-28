package com.tree.community.service;

import com.tree.community.dto.CommentDTO;
import com.tree.community.dto.ResultDTO;
import com.tree.community.enums.CommentTypeEnum;
import com.tree.community.enums.NotificationStatusEnum;
import com.tree.community.enums.NotificationTypeEnum;
import com.tree.community.mapper.*;
import com.tree.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public ResultDTO insert(Comment comment, User commentator) {
        if(comment.getParentId() == null || comment.getParentId() == 0){
            return ResultDTO.errorOf(2002,"未选择任何问题或评论进行回复");
        }

        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                return ResultDTO.errorOf(2003,"回复的评论不存在了，要不换个试试？");
            }
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if(question == null){
                return ResultDTO.errorOf(2004,"回复的帖子不存在了，要不换个试试？");
            }
            commentMapper.insertSelective(comment);
            //增加评论的那条评论的评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);

            //创建通知
            createNotify(comment, dbComment.getCommentator(), commentator.getNickName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
            return ResultDTO.okOf();
        }else{
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                return ResultDTO.errorOf(2004,"回复的帖子不存在了，要不换个试试？");
            }
            commentMapper.insertSelective(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);

            //创建通知
            createNotify(comment,question.getCreator(),commentator.getNickName(),question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
            return ResultDTO.okOf();
        }

    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if(receiver == comment.getCommentator()){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size() == 0){
            return new ArrayList<>();
        }

        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人并转成Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换 comment 为 commentDto
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return  commentDTOS;

    }
}
