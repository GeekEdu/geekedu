package com.zch.oss.domain.query;

// import com.zch.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

// @EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "媒资搜索条件")
// public class MediaQuery extends PageQuery {
public class MediaQuery{

    @ApiModelProperty("媒资名称关键字")
    private String mediaName;

}
