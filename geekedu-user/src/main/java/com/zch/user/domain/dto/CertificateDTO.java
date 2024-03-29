package com.zch.user.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/29
 */
@Data
public class CertificateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 证书名
     */
    private String name;

    /**
     * 证书参数
     */
    private String params;

    /**
     * 模版图片
     */
    private String templateImage;

    /**
     * 授予人数
     */
    private Integer userCount;

    private Long userId;

    private LocalDateTime createdTime;

}
