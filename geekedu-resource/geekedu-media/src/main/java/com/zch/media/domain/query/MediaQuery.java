package com.zch.media.domain.query;

import com.zch.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "媒资搜索条件")
public class MediaQuery extends PageQuery {
    @ApiModelProperty("媒资名称关键字")
    private String name;
}
