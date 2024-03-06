package com.zch.api.vo.course.record;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TestVO extends BaseVO {

    private String name = "";

    private String url;

}
