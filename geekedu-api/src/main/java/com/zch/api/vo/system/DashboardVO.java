package com.zch.api.vo.system;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DashboardVO extends BaseVO {

    private Integer lastMonthPaidSum;

    private Integer thisMonthPaidSum;

    private Integer todayPaidSum;

    private Integer todayPaidUserNum;

    private Integer todayRegisterUserCount;

    private Integer userCount;

    private Integer yesterdayPaidSum;

    private Integer yesterdayPaidUserNum;

}
