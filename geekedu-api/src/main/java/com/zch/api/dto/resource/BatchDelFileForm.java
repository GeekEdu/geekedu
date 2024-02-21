package com.zch.api.dto.resource;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/21
 */
@Data
public class BatchDelFileForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private List<String> ids = new ArrayList<>(0);

}
