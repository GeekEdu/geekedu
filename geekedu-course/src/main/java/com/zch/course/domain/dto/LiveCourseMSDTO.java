package com.zch.course.domain.dto;

import com.zch.common.meilisearch.annotation.MsField;
import com.zch.common.meilisearch.annotation.MsIndex;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/4/2
 */
@Data
@MsIndex(uid = "liveCourse", primaryKey = "id")
public class LiveCourseMSDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    @MsField(openSort = true, openFilter = true, key = "id", description = "主键")
    private Integer id;

    /**
     * 标题
     */
    @MsField(openSort = true, openFilter = true, key = "title", description = "标题")
    private String title;

    /**
     * 封面
     */
    @MsField(openSort = true, openFilter = true, key = "cover", description = "封面")
    private String cover;

    /**
     * 简介
     */
    @MsField(openSort = true, openFilter = true, key = "intro", description = "简介")
    private String intro;

}
