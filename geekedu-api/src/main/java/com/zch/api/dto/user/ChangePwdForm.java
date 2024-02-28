package com.zch.api.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@Data
public class ChangePwdForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String oldPassword;

    private String newPassword;

    private String newPasswordConfirm;

}
