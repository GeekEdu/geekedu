package com.zch.api.dto.label;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Poison02
 * @date 2024/1/7
 */
@Data
@ApiModel(description = "分类表单操作实体")
public class CategoryForm {

    @ApiModelProperty(value = "分类id", example = "129445535", required = false)
    private Long id;

    @ApiModelProperty(value = "分类名", example = "后端开发", required = false)
    private String name;

    @ApiModelProperty(value = "分类类型", example = "课程", required = false)
    private String type;

}
