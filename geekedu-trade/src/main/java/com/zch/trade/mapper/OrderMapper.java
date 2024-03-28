package com.zch.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.api.vo.trade.order.SellCountTopVO;
import com.zch.trade.domain.dto.OrderCountDTO;
import com.zch.trade.domain.dto.PayCountDTO;
import com.zch.trade.domain.po.Order;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/9
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 创建订单数
     * @return
     */
    List<OrderCountDTO> queryOrderCount();

    /**
     * 支付订单数
     * @return
     */
    List<OrderCountDTO> queryPayCount();

    /**
     * 每日收入
     * @return
     */
    List<PayCountDTO> queryPayMoney();

    /**
     * 销售额Top总量
     * @param startAt
     * @param endAt
     * @param goodsType
     * @return
     */
    long sellTopCount(
                      @Param("startAt") LocalDateTime startAt,
                      @Param("endAt") LocalDateTime endAt,
                      @Param("goodsType") Integer goodsType);

    /**
     * 销售额Top数据
     * @param pageNum
     * @param pageSize
     * @param startAt
     * @param endAt
     * @param goodsType
     * @return
     */
    List<SellCountTopVO> getSellTopPage(@Param("pageNum") Integer pageNum,
                                        @Param("pageSize") Integer pageSize,
                                        @Param("startAt") LocalDateTime startAt,
                                        @Param("endAt") LocalDateTime endAt,
                                        @Param("goodsType") Integer goodsType);

    /**
     * 支付订单数
     * @param startAt
     * @param endAt
     * @return
     */
    List<OrderCountDTO> queryFixedPayCount(LocalDateTime startAt, LocalDateTime endAt);

    /**
     * 支付人数
     * @param startAt
     * @param endAt
     * @return
     */
    List<OrderCountDTO> queryFixedPayNum(LocalDateTime startAt, LocalDateTime endAt);

    /**
     * 支付总金额
     * @param startAt
     * @param endAt
     * @return
     */
    List<PayCountDTO> queryFixedPayAmount(LocalDateTime startAt, LocalDateTime endAt);

    /**
     * 客单均价
     * @param startAt
     * @param endAt
     * @return
     */
    List<PayCountDTO> queryFixedPayAvg(LocalDateTime startAt, LocalDateTime endAt);

}
