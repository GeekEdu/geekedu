package com.zch.api.vo.path;

import com.zch.api.dto.path.StepForm;
import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LearnPathFullVO extends BaseVO {

    private PageResult.Data<LearnPathVO> data = new PageResult.Data<>();

    private Map<Integer, List<StepForm>> steps = new HashMap<>(0);

}
