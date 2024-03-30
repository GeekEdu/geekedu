package com.zch.api.dto.user.certificate;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/30
 */
@Data
public class CancelForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Long> ids = new ArrayList<>(0);

}
