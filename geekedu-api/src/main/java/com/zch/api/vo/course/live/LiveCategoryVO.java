package com.zch.api.vo.course.live;

import com.zch.api.vo.label.CategoryVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
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
public class LiveCategoryVO extends BaseVO {

    private List<CategoryVO> categories = new ArrayList<>(0);

    private List<UserSimpleVO> teachers = new ArrayList<>(0);

}
