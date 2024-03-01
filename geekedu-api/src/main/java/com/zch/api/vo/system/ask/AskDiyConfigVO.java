package com.zch.api.vo.system.ask;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AskDiyConfigVO extends BaseVO {

    /**
     * 是否允许悬赏积分
     */
    private Boolean enableRewardScore = true;

    /**
     * 自定义内容
     */
    private String diyContent;

}
