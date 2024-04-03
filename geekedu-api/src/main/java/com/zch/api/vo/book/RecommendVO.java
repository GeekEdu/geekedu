package com.zch.api.vo.book;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/4/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendVO extends BaseVO {

    /**
     * id
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 阅读次数
     */
    private Long readCount;

}
