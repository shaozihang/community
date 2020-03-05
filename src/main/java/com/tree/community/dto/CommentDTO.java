package com.tree.community.dto;

import com.tree.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer likeCount;
    private Integer commentCount;
    private String content;
    private User user;
    private Long targetUserId;
    private String targetUserName;
    private Integer likeStatus;
}
