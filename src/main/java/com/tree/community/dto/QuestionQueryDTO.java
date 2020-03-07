package com.tree.community.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private String search;
    private String tag;
    private Integer page;
    private Integer size;
    private String type;
    private Integer sort;
}
