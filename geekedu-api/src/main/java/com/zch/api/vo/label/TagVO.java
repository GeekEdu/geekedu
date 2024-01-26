package com.zch.api.vo.label;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TagVO extends BaseVO {

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String name;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 父级id
     */
    private Integer parentId;

}
