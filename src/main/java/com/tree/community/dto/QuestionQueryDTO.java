package com.tree.community.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionQueryDTO {
    private String search;
    private String tag;
    private Integer page;
    private Integer size;
    private String type;
    private Integer sort;
    private List<Long> quIds;
}
