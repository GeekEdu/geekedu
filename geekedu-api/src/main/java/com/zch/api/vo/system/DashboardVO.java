package com.zch.api.vo.system;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/1/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DashboardVO extends BaseVO {

    /**
     * 上月收入
     */
    private BigDecimal lastMonthPaidSum;

    /**
     * 本月收入
     */
    private BigDecimal thisMonthPaidSum;

    /**
     * 今日收入
     */
    private BigDecimal todayPaidSum;

    /**
     * 今日支付用户数
     */
    private Long todayPaidUserNum;

    /**
     * 今日注册用户数
     */
    private Long todayRegisterUserCount;

    /**
     * 总学员数
     */
    private Long userCount;

    /**
     * 昨日支付数
     */
    private Long yesterdayPaidSum;

    /**
     * 昨日支付用户数
     */
    private Long yesterdayPaidUserNum;

}
