package com.zch.label.domain.query;

import com.zch.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Poison02
 * @date 2024/1/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("分类标签条件查询实体")
public class CategoryTagQuery extends PageQuery {

    @ApiModelProperty(value = "分类id", example = "14894575")
    private Long categoryId;

    @ApiModelProperty(value = "分类名", example = "后端开发")
    private String categoryName;

    @ApiModelProperty(value = "分类类型", example = "课程分类")
    private String type;

    @ApiModelProperty(value = "标签名id", example = "147854355")
    private Long tagId;

    @ApiModelProperty(value = "标签名", example = "SpringBoot")
    private String tagName;

}
