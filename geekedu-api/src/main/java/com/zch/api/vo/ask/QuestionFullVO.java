package com.zch.api.vo.ask;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionFullVO extends BaseVO {

    private QuestionVO question = new QuestionVO();

    private List<AnswerAndCommentsVO> answer = new ArrayList<>(0);

    /**
     * 是否是管理员
     */
    private Boolean isAdmin = false;

    /**
     * 是否点赞
     */
    private Boolean isThumb = false;

}
