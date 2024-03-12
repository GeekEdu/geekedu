package com.zch.api.vo.course.live;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LiveChapterVO extends BaseVO {

    private Integer id;

    private String name;

    private Integer courseId;

    private Integer sort;

}
