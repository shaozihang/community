package com.tree.community.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLikeExtMapper {
    void deleteByLikedUserId(List<Long> ids);
}
