package com.zch.book.domain.dto;

import com.zch.common.meilisearch.annotation.MsField;
import com.zch.common.meilisearch.annotation.MsIndex;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/4/2
 */
@Data
@MsIndex(uid = "book", primaryKey = "id")
public class BookMSDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    @MsField(openSort = true, openFilter = true, key = "id", description = "主键")
    private Integer id;

    /**
     * 电子书名字
     */
    @MsField(openSort = true, openFilter = true, key = "name", description = "电子书名字")
    private String name;

    /**
     * 简短介绍
     */
    @MsField(openSort = true, openFilter = true, key = "shortDesc", description = "简短介绍")
    private String shortDesc;

    /**
     * 封面链接
     */
    @MsField(openSort = true, openFilter = true, key = "coverLink", description = "封面链接")
    private String coverLink;

}
