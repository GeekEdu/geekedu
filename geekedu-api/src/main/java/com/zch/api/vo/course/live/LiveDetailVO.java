package com.zch.api.vo.course.live;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LiveDetailVO extends BaseVO {

    private LiveCourseVO course;

    private List<LiveChapterVO> chapters = new ArrayList<>(0);

    Map<Integer, List<LiveVideoVO>> videos = new HashMap<>(0);

    private Boolean isBuy = true;

}
