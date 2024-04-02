package com.zch.api.vo.system.search;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/4/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchFullVO extends BaseVO {

    private Long total = 0L;

    private List<SearchVO> data = new ArrayList<>(0);

}
