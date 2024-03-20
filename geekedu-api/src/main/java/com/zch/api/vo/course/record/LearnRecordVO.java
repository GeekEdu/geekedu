package com.zch.api.vo.course.record;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LearnRecordVO extends BaseVO {

    private Integer id;

    private Integer courseId;

    private Integer videoId;

    private String type;

    private Long userId;

    private Integer duration;

}
