package com.zch.api.vo.exam.practice;

import com.zch.api.vo.exam.PracticeVO;
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
 * @date 2024/3/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PracticeFrontVO extends BaseVO {

    /**
     * 一级分类
     */
    private List<CategoryFirstVO> categories = new ArrayList<>(0);

    /**
     * 二级分类 一级分类id为 key，一级章节对应二级分类列表为 value
     */
    private Map<Integer, List<CategorySecondVO>> childrenCategories = new HashMap<>(0);

    /**
     * 练习 数据
     */
    private PageResult.Data<PracticeVO> data = new PageResult.Data<>();

}
