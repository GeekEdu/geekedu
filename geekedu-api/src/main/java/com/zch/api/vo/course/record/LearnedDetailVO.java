package com.zch.api.vo.course.record;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LearnedDetailVO extends BaseVO {

    private Integer id;

    private String title;

    private Integer duration;

    private Integer total;

    private Integer videoId;

}
