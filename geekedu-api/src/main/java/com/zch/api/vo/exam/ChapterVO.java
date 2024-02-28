package com.zch.api.vo.exam;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChapterVO extends BaseVO {

    private Integer id;

    private String name;

    private Integer practiceId;

    private Long questionCount;

    private Integer sort;

}
