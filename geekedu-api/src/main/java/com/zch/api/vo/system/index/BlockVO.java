package com.zch.api.vo.system.index;

import com.zch.common.mvc.entity.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Poison02
 * @date 2024/3/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockVO<T> extends BaseVO {

    private Integer id;

    private String sign;

    private BlockItemVO<T> items;

}
