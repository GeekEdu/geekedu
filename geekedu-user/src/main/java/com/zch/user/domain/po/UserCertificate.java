package com.zch.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户证书关联表
 * @author Poison02
 * @date 2024/3/29
 */
@Data
@TableName("user_certificate")
public class UserCertificate {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long userId;

    private Integer cId;

    private LocalDateTime createdTime;

}
