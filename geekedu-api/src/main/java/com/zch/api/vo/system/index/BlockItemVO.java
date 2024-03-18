package com.zch.api.vo.system.index;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BlockItemVO<T> extends BaseVO {

    private String title;

    private List<T> items = new ArrayList<>(0);

}
