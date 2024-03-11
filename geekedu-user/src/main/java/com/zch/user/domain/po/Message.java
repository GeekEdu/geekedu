package com.zch.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/11
 */
@Data
@TableName("message")
public class Message {

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    private Long userId;

    private String message;

    private LocalDateTime createdTime;

    private LocalDateTime readTime;

    private Boolean isRead;

}
