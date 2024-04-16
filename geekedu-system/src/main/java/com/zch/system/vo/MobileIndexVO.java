package com.zch.system.vo;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MobileIndexVO extends BaseVO {

    private String type;

    private Object data;

    private Boolean checked = false;

    private String listType;

    private Boolean showMore = true;

}
