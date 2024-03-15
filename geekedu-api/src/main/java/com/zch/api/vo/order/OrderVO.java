package com.zch.api.vo.order;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/2/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderVO extends BaseVO {

    private String orderId = "202403151330123456";

    private String statusText = "未支付";

    private String qrCode;

    private Integer status;

}
