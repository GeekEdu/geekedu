package com.zch.api.vo.ask;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentsSimpleVO extends BaseVO {

    private String content;

    private LocalDateTime createdTime;

    private Integer id;

    private Long userId;

}
