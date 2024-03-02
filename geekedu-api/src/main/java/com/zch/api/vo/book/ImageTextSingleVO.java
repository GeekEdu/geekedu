package com.zch.api.vo.book;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageTextSingleVO extends BaseVO {

    private Boolean isBuy = true;

    private Boolean isCollect = false;

    private Boolean isThumb = false;

    private ImageTextVO imageText;

}
