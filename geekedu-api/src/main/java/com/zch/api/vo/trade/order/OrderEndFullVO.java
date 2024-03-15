package com.zch.api.vo.trade.order;

import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderEndFullVO extends BaseVO {

    private Map<Long, UserSimpleVO> users = new HashMap<>(0);

    private Map<Integer, Long> countMap = new HashMap<>(0);

    private PageResult.Data<Boolean> data = new PageResult.Data();

}
