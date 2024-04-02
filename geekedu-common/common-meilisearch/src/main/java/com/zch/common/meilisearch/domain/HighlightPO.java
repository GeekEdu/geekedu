package com.zch.common.meilisearch.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/4/2
 */
@Data
public class HighlightPO implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 高亮词
     */
    private String highlight;

}
