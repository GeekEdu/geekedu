package com.zch.api.vo.book;

import com.zch.common.mvc.entity.BaseVO;
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
public class ArticleFullVO extends BaseVO {

    private EBookVO book;

    private EBookArticleVO article;

    private List<EBookChapterVO> chapters;

    private List<EBookArticleVO> articles = new ArrayList<>(0);

    private Map<Integer, List<EBookArticleVO>> articleMap = new HashMap<>(0);

    private Boolean canSee = true;

    private Boolean isBuy = true;

    private Integer prevId;

    private Integer nextId;

}
