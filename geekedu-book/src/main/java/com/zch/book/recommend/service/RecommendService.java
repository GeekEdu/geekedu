package com.zch.book.recommend.service;

import com.zch.api.vo.book.RecommendVO;
import com.zch.book.recommend.dto.ProductDTO;
import com.zch.book.recommend.dto.RelateDTO;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/4/3
 */
public interface RecommendService {

    /**
     * 获取所有商品数据
     * @return
     */
    List<ProductDTO> getProductData();

    /**
     * 获取用户购买商品数据
     * @return
     */
    List<RelateDTO> getRelateData();

    /**
     * 返回推荐数据
     * @param userId
     * @param num
     * @return
     */
    List<RecommendVO> recommendGoods(Long userId, Integer num);

}
