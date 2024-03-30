package com.zch.user.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/30
 */
@Data
public class UserCertDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer cId;

    private String cNum;

    private Long userId;

    private LocalDateTime createdTime;

}
