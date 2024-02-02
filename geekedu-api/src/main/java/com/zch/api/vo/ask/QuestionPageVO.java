package com.zch.api.vo.ask;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionPageVO extends BaseVO {

    private Integer id;

    private String title;

    private String category;

    private Long viewCount;

    private Long commentCount;

    private Long userId;

    private Integer rewardScore;

    private Boolean questionStatus;

    private LocalDateTime createdTime;

}
