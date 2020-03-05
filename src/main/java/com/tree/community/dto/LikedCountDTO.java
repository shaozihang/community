package com.tree.community.dto;

import lombok.Data;

@Data
public class LikedCountDTO {
    private Long id;
    private Integer type;
    private Integer likedCount;

    public LikedCountDTO(){

    }

    public LikedCountDTO(Long id, Integer type, Integer likedCount) {
        this.id = id;
        this.type = type;
        this.likedCount = likedCount;
    }
}
