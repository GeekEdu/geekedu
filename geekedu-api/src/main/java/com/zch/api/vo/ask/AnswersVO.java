package com.zch.api.vo.ask;

import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AnswersVO extends BaseVO {

    private Integer id;

    private Integer questionId;

    private Long userId;

    private String content;

    private String images = "";

    private List<String> imageList = new ArrayList<>(0);

    private Long thumbCount;

    /**
     * 评论数
     */
    private Long commentCount;

    private Boolean isCorrect;

    private UserSimpleVO user;

    private LocalDateTime createdTime;

}
