package com.zch.api.dto.resource;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/20
 */
@Data
public class BatchDelVideoForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * id列表
     */
    private List<Integer> ids;

}
