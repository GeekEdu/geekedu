package com.zch.api.dto.ask;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/20
 */
@Data
public class QuestionDeleteBatchForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private List<Integer> ids = new ArrayList<>(0);

}
