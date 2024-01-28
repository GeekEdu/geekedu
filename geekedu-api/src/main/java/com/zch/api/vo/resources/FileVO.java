package com.zch.api.vo.resources;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileVO extends BaseVO {

    private Long id;

    private String fileName;

    private String link;

}
