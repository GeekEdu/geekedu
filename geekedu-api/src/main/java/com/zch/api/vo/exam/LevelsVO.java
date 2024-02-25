package com.zch.api.vo.exam;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LevelsVO extends BaseVO {

    private Integer id;

    private String name;

    private Integer sort;

    private LocalDateTime createdTime;

}
