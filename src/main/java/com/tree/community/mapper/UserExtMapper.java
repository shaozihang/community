package com.tree.community.mapper;

import com.tree.community.model.User;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserExtMapper {
    void addScoreOfQu(Long id);//id为作者

    void addCommentatorScore(Long id);//id为评论者

    void addAuthorScore(Long id);//id为作者

    void reduceAuthorScore(User user);

    void reduceCommentatorScore(Long id);//id为评论者
}
