package com.zch.api.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/31
 */
@Data
public class WxRegForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String password;

    private String rePassword;

}
