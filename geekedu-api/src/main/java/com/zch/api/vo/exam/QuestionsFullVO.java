package com.zch.api.vo.exam;

import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionsFullVO extends BaseVO {

    private PageResult.Data<QuestionsVO> data = new PageResult.Data<>();

    private List<TypesVO> types = new ArrayList<>(0);

    private List<LevelsVO> levels = new ArrayList<>(0);

    private List<TagsVO> categories = new ArrayList<>(0);

}
