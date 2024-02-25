package com.zch.api.dto.exam;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@Data
public class DeleteBatchQuestions implements Serializable {

    private static final Long serialVersionUID = 1L;

    private List<Integer> ids = new ArrayList<>(0);

}
