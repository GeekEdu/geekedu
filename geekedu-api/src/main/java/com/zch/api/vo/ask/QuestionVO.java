package com.zch.api.vo.ask;

import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionVO extends BaseVO {

    private Integer id;

    /**
     * 问题标题
     */
    private String title;

    private CategorySimpleVO category;

    /**
     * 浏览次数
     */
    private Long viewCount;

    /**
     * 回答个数
     */
    private Long answerCount;

    /**
     * 提问用户简单信息
     */
    private UserSimpleVO user;

    /**
     * 悬赏积分
     */
    private Integer rewardScore;

    /**
     * 问题状态 0-未解决 1-已解决
     */
    private Boolean questionStatus;

    /**
     * 根据 questionStatus 判断的文本
     */
    private String statusText;

    private LocalDateTime createdTime;

    /**
     * 图片，使用 , 分割
     */
    private String images;

    private List<String> imagesList = new ArrayList<>(0);

}
