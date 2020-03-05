package com.tree.community.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private long questionId;
    private long parentId;
    private long targetId;
    private String content;
    private Integer type;
}
