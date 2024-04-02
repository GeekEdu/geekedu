package com.zch.api.vo.system.search;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全文检索VO
 * @author Poison02
 * @date 2024/4/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchVO extends BaseVO {

    private Integer id;

    private Integer resourceId;

    private String resourceType;

    private String title;

    private String description;

}
