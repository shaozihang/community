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

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private UserLikeService userLikeService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public ResultDTO insert(Comment comment, User commentator,Long questionId) {
        if(comment.getParentId() == null || comment.getParentId() == 0){
            return ResultDTO.errorOf(2002,"未选择任何问题或评论进行回复");
        }

        if(comment.getType() == CommentTypeEnum.COMMENT.getType() || comment.getType() == CommentTypeEnum.SUBCOMMENT.getType()){
            Comment targetComment = commentMapper.selectByPrimaryKey(comment.getTargetId());
            if(targetComment == null){
                return ResultDTO.errorOf(2003,"回复的评论不存在了，要不换个试试？");
            }
            Question question = questionMapper.selectByPrimaryKey(questionId);
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
            createNotify(comment, targetComment.getCommentator(), commentator.getNickName(), targetComment.getContent(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
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

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type,HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("user");
        List<Comment> comments = null;
        if(type.getType() == 1){
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria()
                    .andParentIdEqualTo(id)
                    .andTypeEqualTo(type.getType());
            commentExample.setOrderByClause("gmt_create desc");
            comments = commentMapper.selectByExample(commentExample);
        }else {
            comments = commentExtMapper.selectByType(id);
        }
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
            if(comment.getType() == 3){
                Comment targetUserComment = commentMapper.selectByPrimaryKey(comment.getTargetId());
                User user = userMapper.selectByPrimaryKey(targetUserComment.getCommentator());
                commentDTO.setTargetUserId(user.getId());
                commentDTO.setTargetUserName(user.getNickName());
            }
            if(currentUser != null){
                Integer status = userLikeService.selectlikeStatus(commentDTO.getId(), currentUser.getId(), 2);
                commentDTO.setLikeStatus(status);
            }else{
                commentDTO.setLikeStatus(0);
            }
            Integer likeCount = redisService.selectlikeCount(commentDTO.getId(), 2);
            if(likeCount != null){
                commentDTO.setLikeCount(commentDTO.getLikeCount()+likeCount);
            }
            return commentDTO;
        }).collect(Collectors.toList());
        return  commentDTOS;

    }
}
