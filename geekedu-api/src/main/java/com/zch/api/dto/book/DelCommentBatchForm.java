package com.zch.api.dto.book;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/4
 */
@Data
public class DelCommentBatchForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private List<Integer> ids = new ArrayList<>(0);

}
