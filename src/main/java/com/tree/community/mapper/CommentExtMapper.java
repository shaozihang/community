package com.tree.community.mapper;

import com.tree.community.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentExtMapper {
    int incCommentCount(Comment Comment);

    List<Comment> selectByType(Long id);

    List<Long> getCommentatorByQuId(Long questionId);

    void incLikeCount(Comment comment);

    void deleteByParentId(Long commentId);

    void decCommentCount(Long id);//一级评论的id

    void updateReplyByTargetId(Comment comment1);//通过回复的id和回复的用户id修改对应的回复
}
