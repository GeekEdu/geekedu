package com.zch.api.vo.exam.paper;

import com.zch.api.vo.exam.PapersVO;
import com.zch.api.vo.exam.practice.CategoryFirstVO;
import com.zch.api.vo.exam.practice.CategorySecondVO;
import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaperFrontVO extends BaseVO {

    private PageResult.Data<PapersVO> data = new PageResult.Data<>();

    /**
     * 一级分类
     */
    private List<CategoryFirstVO> categories = new ArrayList<>(0);

    /**
     * 二级分类 一级分类id为 key，一级章节对应二级分类列表为 value
     */
    private Map<Integer, List<CategorySecondVO>> childrenCategories = new HashMap<>(0);

}
