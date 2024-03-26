package com.zch.api.vo.trade.seckill;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SecKillV2VO extends BaseVO {

    /**
     * 订单id 若下了秒杀单就会有
     */
    private String orderId;

    /**
     * 订单状态 下了秒杀单就会有
     */
    private Integer orderStatus;

    /**
     * 秒杀详情
     */
    private SecondKillVO data = new SecondKillVO();

}
