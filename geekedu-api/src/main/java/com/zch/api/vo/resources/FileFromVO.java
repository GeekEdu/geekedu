package com.zch.api.vo.resources;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上传图片来源 VO
 * @author Poison02
 * @date 2024/2/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileFromVO extends BaseVO {

    /**
     * 图片来源 key
     */
    private Integer key;

    /**
     * 图片来源描述
     */
    private String name;

}
