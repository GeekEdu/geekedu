package com.zch.label.domain.vo;

import com.zch.common.domain.vo.PageReqVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("查找分类下的所有标签请求实体")
public class CategoryReqVO extends PageReqVO {

    public CategoryReqVO() {
        super();
    }

    @ApiModelProperty(value = "标签id", example = "1489573983")
    private Long id;

    // 用于查询所有标签时的 标签id集合
    private List<Long> ids;
}
