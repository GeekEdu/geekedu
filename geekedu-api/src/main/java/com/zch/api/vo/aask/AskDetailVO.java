package com.zch.api.vo.aask;

import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/1/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AskDetailVO extends BaseVO {

    private Integer id;

    private UserSimpleVO userInfo;

    private String title;

    private String content;

    private Long viewCount;

    private Long commentCount;

    private CategorySimpleVO category;

    private LocalDateTime createdTime;

}
