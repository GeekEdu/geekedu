package com.zch.api.vo.book;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

/**
 * @author Poison02
 * @date 2024/3/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EBookFullVO extends BaseVO {

    private EBookVO book = new EBookVO();

    private Boolean isBuy = false;

    private List<EBookChapterVO> chapters = new ArrayList<>(0);

    private List<EBookArticleVO> articles = new ArrayList<>(0);

    private Map<Integer, List<EBookArticleVO>> articleMap = new HashMap<>(0);

}
