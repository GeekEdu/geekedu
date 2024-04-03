package com.zch.book.recommend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zch.api.feignClient.trade.TradeFeignClient;
import com.zch.api.vo.book.RecommendVO;
import com.zch.api.vo.order.OrderVO;
import com.zch.book.domain.po.EBook;
import com.zch.book.recommend.dto.ProductDTO;
import com.zch.book.recommend.dto.RelateDTO;
import com.zch.book.recommend.service.RecommendService;
import com.zch.book.service.IEBookService;
import com.zch.book.service.IImageTextService;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.mvc.result.Response;
import com.zch.common.satoken.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/4/3
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final IEBookService bookService;

    private final IImageTextService imageTextService;

    private final TradeFeignClient tradeFeignClient;

    @Override
    public List<ProductDTO> getProductData() {
        List<EBook> bookList = bookService.list(new LambdaQueryWrapper<EBook>()
                .gt(EBook::getPrice, 0.00));
        if (ObjectUtils.isNotNull(bookList) && CollUtils.isNotEmpty(bookList)) {
            List<ProductDTO> vo = new ArrayList<>();
            bookList.forEach(item -> {
                ProductDTO dto = new ProductDTO();
                dto.setProductId(Long.valueOf(item.getId()));
                dto.setProductName(item.getName());
                dto.setProductPrice(String.valueOf(item.getPrice()));
                vo.add(dto);
            });
            return vo;
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<RelateDTO> getRelateData() {
        Long userId = UserContext.getLoginId();
        List<RelateDTO> relateDTOList = new ArrayList<>();
        Response<List<OrderVO>> result = tradeFeignClient.queryPayOrderList(userId);
        if (ObjectUtils.isNotNull(result) && ObjectUtils.isNotNull(result.getData()) && CollUtils.isNotEmpty(result.getData())) {
            Map<String, List<OrderVO>> orderList = result.getData().stream().collect(Collectors.groupingBy(OrderVO::getOrderId));
            Map<Integer, List<OrderVO>> goodsList = result.getData().stream().collect(Collectors.groupingBy(OrderVO::getGoodsId));
            // 遍历订单，生成预处理数据
            for (OrderVO order : result.getData()) {
                String orderId = order.getOrderId();
                // 遍历订单商品
                for (OrderVO item : orderList.getOrDefault(orderId, Collections.emptyList())) {
                    Integer goodsId = item.getGoodsId();
                    EBook book = bookService.getById(goodsId);
                    Integer categoryId = book.getCategoryId();
                    RelateDTO relateDTO = new RelateDTO();
                    relateDTO.setUserId(item.getUserId());
                    relateDTO.setProductId(Long.valueOf(goodsId));
                    relateDTO.setCategoryId(Long.valueOf(categoryId));
                    // 通过计算商品购买次数，来建立相似度
                    List<OrderVO> list = goodsList.getOrDefault(goodsId, Collections.emptyList());
                    int sum = list.stream().mapToInt(OrderVO::getGoodsCount).sum();
                    relateDTO.setIndex(sum);
                    relateDTOList.add(relateDTO);
                }
            }
            return relateDTOList;
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<RecommendVO> recommendGoods(Long userId, Integer num) {
        return null;
    }
}
