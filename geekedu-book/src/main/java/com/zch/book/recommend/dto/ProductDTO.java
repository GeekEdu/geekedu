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
public class ProductDTO {

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 商品价格
     */
    private String productPrice;

}
