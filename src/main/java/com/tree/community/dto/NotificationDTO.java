package com.tree.community.dto;

import com.tree.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;
}
