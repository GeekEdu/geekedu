package com.zch.api.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class EditNickNameForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nickName;

}
