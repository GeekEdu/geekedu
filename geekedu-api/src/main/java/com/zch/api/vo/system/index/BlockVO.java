package com.zch.api.vo.system.index;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BlockVO<T> extends BaseVO {

    private Integer id;

    private String sign;

    private BlockItemVO<T> items;

}
