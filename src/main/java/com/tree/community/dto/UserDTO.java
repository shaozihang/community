package com.tree.community.dto;

import com.tree.community.model.Useroauths;
import lombok.Data;

@Data
public class UserDTO {
    private String nickName;
    private String account;
    private String password;
    private String phone;
    private String code;
    private Integer flag;
    private String oauthsId;
    private String oauthsType;
}
