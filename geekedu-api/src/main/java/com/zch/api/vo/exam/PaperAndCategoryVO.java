package com.zch.api.vo.exam;

import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PaperAndCategoryVO extends BaseVO {

    private PageResult.Data<PapersVO> data = new PageResult.Data<>();

    private List<CTagsVO> categories = new ArrayList<>(0);

}
