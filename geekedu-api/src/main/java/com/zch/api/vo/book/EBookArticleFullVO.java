package com.zch.api.vo.book;

import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EBookArticleFullVO extends BaseVO {

    private PageResult.Data<EBookArticleVO> data = new PageResult.Data<>();

    private List<EBookChapterVO> chapters;

}
