package com.zch.api.dto.book;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/2/24
 */
@Data
public class EBookChapterForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Integer bookId;

    private Integer sort;

}
