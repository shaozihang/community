package com.tree.community.enums;

public enum QuestionTypeEnum {
    QUESTION(1,"提问"),
    SHARE(2,"分享"),
    SUGGEST(3,"建议"),
    DISCUSS(4,"讨论"),
    DYNAMIC(5,"动态"),
    OTHER(6,"其他"),
    NOTICE(7,"公告");

    private Integer type;
    private String name;

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    QuestionTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String nameOfType(Integer type){
        for(QuestionTypeEnum QuestionTypeEnum : QuestionTypeEnum.values()){
            if(QuestionTypeEnum.getType() == type){
                return QuestionTypeEnum.getName();
            }
        }
        return "";
    }
}
