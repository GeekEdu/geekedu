package com.zch.api.vo.system;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/2/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GraphVO extends BaseVO {

    private Map<String, Integer> userRegister = new HashMap<>(0);

    private Map<String, Integer> orderCreated = new HashMap<>(0);

    private Map<String, Integer> orderPaid = new HashMap<>(0);

    private Map<String, Integer> orderSum = new HashMap<>(0);

}
