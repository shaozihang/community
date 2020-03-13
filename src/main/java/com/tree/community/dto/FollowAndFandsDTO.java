package com.tree.community.dto;

import com.tree.community.model.User;
import lombok.Data;

@Data
public class FollowAndFandsDTO {
    private User user;
    private Integer followStatus;
    private Integer followOtherStatus;
}
