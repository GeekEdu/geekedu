package com.zch.api.vo.path;

import com.zch.api.dto.path.StepForm;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LearnPathDetailVO extends BaseVO {

    private LearnPathVO data;

    private List<StepForm> steps = new ArrayList<>(0);

    private Boolean isBuy = true;

}
