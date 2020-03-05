package com.tree.community.mapper;

import com.tree.community.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentExtMapper {
    int incCommentCount(Comment Comment);

    List<Comment> selectByType(Long id);
}
