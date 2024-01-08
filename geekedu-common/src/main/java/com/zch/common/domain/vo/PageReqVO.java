package com.zch.common.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Poison02
 * @date 2024/1/8
 */
@ApiModel("分页请求实体")
@Data
public class PageReqVO {

    @ApiModelProperty(value = "当前页", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页记录条数", example = "10")
    private Integer pageSize = 10;

}
