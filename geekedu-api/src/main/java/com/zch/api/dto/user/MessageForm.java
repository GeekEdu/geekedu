package com.zch.api.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/11
 */
@Data
public class MessageForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String message;

    private List<Long> userIds = new ArrayList<>(0);

}
