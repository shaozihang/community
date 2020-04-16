package com.tree.community.dto;

import com.tree.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
    private Integer likeStatus;
    private String typeName;
    private Integer collectionCount;
    private Boolean essence;
    private Boolean isTop;
}
