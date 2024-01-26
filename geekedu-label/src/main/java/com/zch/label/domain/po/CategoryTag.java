package com.zch.label.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import com.zch.label.enums.CategoryEnum;
import lombok.*;

/**
 * category_tag
 * @author Poison02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("category_tag")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTag extends BaseEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 标签id
     */
    private Integer tagId;

    /**
     * 类型;属于哪种类型的分类，1-录播课，2-直播课，3-图文，4-电子书，5-学习路线
     */
    private CategoryEnum type;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 更新人
     */
    private Long updatedBy;

    /**
     * 是否删除;0-未删除，1-删除
     */
    @TableLogic
    private Boolean isDelete;

    /**
     * 排序字段
     */
    private Integer sort;

}
