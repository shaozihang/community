package com.tree.community.service;

import com.tree.community.dto.CommentDTO;
import com.tree.community.dto.ResultDTO;
import com.tree.community.enums.CommentTypeEnum;
import com.tree.community.enums.NotificationStatusEnum;
import com.tree.community.enums.NotificationTypeEnum;
import com.tree.community.mapper.*;
import com.tree.community.model.*;
import com.tree.community.util.ScoreToGradeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
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

    @Autowired
    private UserExtMapper userExtMapper;

    @Autowired
    private UserLikeExtMapper userLikeExtMapper;

    @Autowired
    private UserLikeMapper userLikeMapper;

    @Transactional
    public ResultDTO insert(Comment comment, User commentator,Long questionId,HttpServletRequest request) {
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
            //评论添加评论者的积分和作者的积分
            if(comment.getCommentator() != targetComment.getCommentator() && comment.getCommentator() != question.getCreator()){
                comment.setAddition(true);
                userExtMapper.addCommentatorScore(comment.getCommentator());
                userExtMapper.addAuthorScore(question.getCreator());
                User user = userMapper.selectByPrimaryKey(comment.getCommentator());
                request.getSession().setAttribute("user",user);
            }
            commentMapper.insertSelective(comment);
            //增加评论的那条评论的评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //创建通知
            createNotify(comment, targetComment.getCommentator(), commentator.getNickName(), targetComment.getContent(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
            return ResultDTO.okOf();
        }else{
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                return ResultDTO.errorOf(2004,"回复的帖子不存在了，要不换个试试？");
            }
            //评论添加评论者的积分和作者的积分
            if(comment.getCommentator() != question.getCreator()){
                comment.setAddition(true);
                userExtMapper.addCommentatorScore(comment.getCommentator());
                userExtMapper.addAuthorScore(question.getCreator());
                User user = userMapper.selectByPrimaryKey(comment.getCommentator());
                request.getSession().setAttribute("user",user);
            }
            commentMapper.insertSelective(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //创建通知
            createNotify(comment,question.getCreator(),commentator.getNickName(),question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
            return ResultDTO.okOf();
        }
    }

    @Transactional
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
        notification.setSenderId(comment.getId());
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
            commentDTO.getUser().setGrade(ScoreToGradeUtils.scoreToGrade(commentDTO.getUser().getScore()));

            if(comment.getType() == 3){
                Comment targetUserComment = commentMapper.selectByPrimaryKey(comment.getTargetId());
                long userId;
                if(targetUserComment == null){
                    userId = Math.abs(comment.getTargetId());
                }else{
                    userId = targetUserComment.getCommentator();
                }
                User user = userMapper.selectByPrimaryKey(userId);
                commentDTO.setTargetUserId(user.getId());
                commentDTO.setTargetUserName(user.getNickName());
            }
            if(currentUser != null){
                Integer status = userLikeService.selectlikeStatus(comment.getQuestionId(),commentDTO.getId(), currentUser.getId(), 2);
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

    @Transactional
    public void deleteComment(Long commentId, Long authorId) {
        userLikeService.transLikedFromRedisToDB();
        userLikeService.transLikedCountFromRedisToDB();
        List<Comment> comments = commentExtMapper.selectByType(commentId);
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        //删除评论
        commentMapper.deleteByPrimaryKey(commentId);
        commentExtMapper.deleteByParentId(commentId);
        Question question = new Question();
        question.setId(comment.getQuestionId());
        question.setCommentCount(comments.size()+1);
        questionExtMapper.decCommentCount(question);
        //删除点赞和删除消息
        List<Long> ids = new ArrayList<>();
        if(comments.size() != 0){
            for (Comment comment1 : comments) {
                ids.add(comment1.getId());
            }
            NotificationExample example = new NotificationExample();
            example.createCriteria()
                    .andSenderIdIn(ids)
                    .andTypeEqualTo(2);
            notificationMapper.deleteByExample(example);
        }
        NotificationExample example1 = new NotificationExample();
        example1.createCriteria()
                .andSenderIdEqualTo(commentId)
                .andTypeEqualTo(1);
        notificationMapper.deleteByExample(example1);
        ids.add(commentId);
        userLikeExtMapper.deleteByLikedUserId(ids);
        //删除发评论者和作者所获积分
        int size = 0;
        if(comment.getAddition()){
            size += 1;
            userExtMapper.reduceCommentatorScore(comment.getCommentator());
        }
        if(comments.size() != 0){
            for (Comment comm : comments) {
                if(comm.getAddition()){
                    size += 1;
                }
            }
        }
        User user = new User();
        user.setId(authorId);
        user.setScore(size);
        userExtMapper.reduceAuthorScore(user);
    }

    @Transactional
    public void deleteComment2(Long commentId, Long authorId, Long parentId) {
        userLikeService.transLikedFromRedisToDB();
        userLikeService.transLikedCountFromRedisToDB();
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        //删除评论
        commentMapper.deleteByPrimaryKey(commentId);
        commentExtMapper.decCommentCount(parentId);
        Question question = new Question();
        question.setId(comment.getQuestionId());
        question.setCommentCount(1);
        questionExtMapper.decCommentCount(question);
        Comment comment1 = new Comment();
        comment1.setCommentator(-comment.getCommentator());
        comment1.setTargetId(comment.getId());
        commentExtMapper.updateReplyByTargetId(comment1);
        //删除消息
        NotificationExample example1 = new NotificationExample();
        example1.createCriteria()
                .andSenderIdEqualTo(commentId)
                .andTypeEqualTo(2);
        notificationMapper.deleteByExample(example1);
        //删除发评论者和作者所获积分
        if(comment.getAddition()){
            userExtMapper.reduceCommentatorScore(comment.getCommentator());
            User user = new User();
            user.setId(authorId);
            user.setScore(1);
            userExtMapper.reduceAuthorScore(user);
        }
        if(comment.getLikeCount() != 0){
            //删除点赞
            UserLikeExample example = new UserLikeExample();
            example.createCriteria()
                    .andLikedUserIdEqualTo(commentId)
                    .andTypeEqualTo(2);
            userLikeMapper.deleteByExample(example);
        }
    }
}