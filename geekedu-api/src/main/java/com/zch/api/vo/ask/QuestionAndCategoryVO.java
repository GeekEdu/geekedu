package com.zch.api.vo.ask;

import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionAndCategoryVO extends BaseVO {

    private PageResult.Data<QuestionVO> data = new PageResult.Data<>();

    private List<CategorySimpleVO> categories;

}
