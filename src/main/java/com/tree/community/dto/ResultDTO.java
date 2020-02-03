package com.tree.community.dto;

import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResultDTO errorOf(Integer code, String message){
        ResultDTO resultDto = new ResultDTO();
        resultDto.setCode(code);
        resultDto.setMessage(message);
        return resultDto;
    }

    public static ResultDTO okOf(){
        ResultDTO resultDto = new ResultDTO();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        return resultDto;
    }

    public static <T> ResultDTO okOf(T t){
        ResultDTO resultDto = new ResultDTO();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        resultDto.setData(t);
        return resultDto;
    }
}
