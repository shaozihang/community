package com.tree.community.enums;

public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2),
    SUBCOMMENT(3);
    private Integer type;

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
