package com.zch.api.vo.course;

import com.zch.api.vo.ask.CommentsVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/2/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseCommentsVO extends BaseVO {

    private PageResult.Data<CommentsVO> data = new PageResult.Data<>();

    private Map<Long, UserSimpleVO> users = new HashMap<>(0);

}
