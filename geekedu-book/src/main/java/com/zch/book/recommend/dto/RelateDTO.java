package com.zch.book.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Poison02
 * @date 2024/4/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelateDTO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 指数
     */
    private Integer index;

}
