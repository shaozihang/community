package com.tree.community.dto;

import lombok.Data;

@Data
public class BookMarkDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean isPrivate;
    private Boolean collected;
    private Integer collectionCount;
    private Long gmtCreate;
}
