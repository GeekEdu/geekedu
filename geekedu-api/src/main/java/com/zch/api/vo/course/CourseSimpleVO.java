package com.zch.api.vo.course;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/2/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseSimpleVO extends BaseVO {

    private Integer id;

    private String title;

}
