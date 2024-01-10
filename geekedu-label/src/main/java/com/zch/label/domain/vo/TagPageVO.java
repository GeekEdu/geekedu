package com.zch.label.domain.vo;

import com.zch.common.domain.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TagPageVO extends BaseVO {

    /**
     * 标签 id
     */
    private Long id;

    /**
     * 标签名
     */
    private String name;

}
