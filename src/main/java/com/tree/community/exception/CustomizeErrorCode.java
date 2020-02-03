package com.tree.community.exception;

public enum CustomizeErrorCode implements ErrorCode{

    QUESTION_NOT_FOUND("您找的问题不在了，要不要换个试试？"),
    READ_NOTIFICATION_FAIL("兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND("消息莫非是不翼而飞了？"),
    FILE_UPLOAD_FAIL("图片上传失败")
    ;
    @Override
    public String getMessage(){
        return message;
    }

    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }
}
