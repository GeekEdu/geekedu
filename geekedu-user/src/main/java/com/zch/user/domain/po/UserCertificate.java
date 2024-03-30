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

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 证书id
     */
    private Integer cId;

    /**
     * 证书编号，每个用户得到的某个证书都有一个编号 由系统随机生成
     */
    private String cNum;

    /**
     * 授予时间
     */
    private LocalDateTime createdTime;

}
