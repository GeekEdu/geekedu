package com.zch.api.vo.user;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PivotVO extends BaseVO {

    private Long administratorId;

    private Integer roleId;

}
