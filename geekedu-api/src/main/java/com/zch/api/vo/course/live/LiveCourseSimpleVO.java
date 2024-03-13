package com.zch.api.vo.course.live;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/3/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LiveCourseSimpleVO extends BaseVO {

    private Integer id;

    private String title;

    private String cover;

    private BigDecimal price;

}
