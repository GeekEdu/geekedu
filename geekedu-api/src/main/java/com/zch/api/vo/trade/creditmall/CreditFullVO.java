package com.zch.api.vo.trade.creditmall;

import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreditFullVO extends BaseVO {

    PageResult.Data<CreditMallVO> goods = new PageResult.Data<>();

    List<GoodsTypeVO> goodsType = new ArrayList<>(0);

}
