package com.zch.api.dto.label;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Poison02
 * @date 2024/1/7
 */
@Data
@ApiModel(description = "标签表单操作实体")
public class TagForm {

    @ApiModelProperty(value = "分类id", example = "135456354", required = false)
    private Long categoryId;

    @ApiModelProperty(value = "标签id", example = "129445535", required = false)
    private Long id;

    @ApiModelProperty(value = "标签名", example = "SpringBoot", required = false)
    private String name;

}
