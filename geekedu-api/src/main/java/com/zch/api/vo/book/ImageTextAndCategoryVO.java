package com.zch.api.vo.book;

import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageTextAndCategoryVO extends BaseVO {

    private PageResult.Data<ImageTextVO> data = new PageResult.Data<>();

    private List<CategorySimpleVO> categories = new ArrayList<>(0);

}
