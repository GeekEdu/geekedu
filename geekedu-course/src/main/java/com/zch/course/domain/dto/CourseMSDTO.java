package com.zch.course.domain.dto;

import com.zch.common.meilisearch.annotation.MsField;
import com.zch.common.meilisearch.annotation.MsIndex;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/4/2
 */
@Data
@MsIndex(uid = "course", primaryKey = "id")
public class CourseMSDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    @MsField(openSort = true, openFilter = true, key = "id", description = "主键")
    private Integer id;

    /**
     * 课程标题
     */
    @MsField(openSort = true, openFilter = true, key = "id", description = "主键")
    private String title;

    /**
     * 课程价格
     */
    @MsField(openSort = true, openFilter = true, key = "price", description = "课程价格")
    private BigDecimal price;

    /**
     * 课程描述
     */
    @MsField(openSort = true, openFilter = true, key = "description", description = "课程描述")
    private String description;

    /**
     * 课程封面
     */
    @MsField(openSort = true, openFilter = true, key = "coverLink", description = "课程封面")
    private String coverLink;

}
