package com.tree.community.mapper;

import com.tree.community.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentExtMapper {
    int incCommentCount(Comment Comment);
}
