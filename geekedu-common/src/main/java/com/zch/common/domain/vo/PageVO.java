package com.zch.common.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/8
 */
@Data
@ApiModel("分页实体")
public class PageVO<T> {

    @ApiModelProperty(value = "总记录条数")
    private long total;

    @ApiModelProperty(value = "当前页")
    private Integer pageNum;

    @ApiModelProperty(value = "每页记录数")
    private Integer pageSize;

    @ApiModelProperty(value = "总页数")
    private Integer pageCount;

    @ApiModelProperty(value = "数据列表")
    private List<T> list;

}
