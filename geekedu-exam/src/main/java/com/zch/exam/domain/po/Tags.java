package com.zch.exam.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import com.zch.exam.enums.ExamTagsEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tags")
public class Tags extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer sort;

    /**
     * 类型 题、试卷
     */
    private ExamTagsEnum type;

    private Integer parentId;

    private Long createdBy;

    private Long updatedBy;

    @TableLogic
    private Boolean isDelete;

}
