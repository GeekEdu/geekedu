package com.zch.api.vo.course.live;

import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LiveFrontVO extends BaseVO {

    private PageResult.Data<LiveCourseVO> data = new PageResult.Data<>();

    private List<CategorySimpleVO> categories = new ArrayList<>(0);

}
