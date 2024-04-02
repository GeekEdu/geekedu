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
@MsIndex(uid = "topic", primaryKey = "id")
public class TopicMSDTO implements Serializable {

    @MsField(openSort = true, openFilter = true, key = "id", description = "主键")
    private Integer id;

    /**
     * 标题
     */
    @MsField(openSort = true, openFilter = true, key = "title", description = "标题")
    private String title;

    /**
     * 封面链接
     */
    @MsField(openSort = true, openFilter = true, key = "coverLink", description = "封面链接")
    private String coverLink;

}
