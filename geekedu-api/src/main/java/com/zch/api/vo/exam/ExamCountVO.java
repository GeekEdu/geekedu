package com.zch.api.vo.exam;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExamCountVO extends BaseVO {

    /**
     * 试卷数 在线考试数
     */
    private long paperCount = 0;

    /**
     * 模拟考试数
     */
    private long mockCount = 0;

    /**
     * 收藏数
     */
    private long collectionCount = 0;

    /**
     * 错题本数
     */
    private long wrongBookCount = 0;

    /**
     * 练习数
     */
    private long practiceCount = 0;

    /**
     * 练习章节数
     */
    private long practiceChapterCount = 0;

}
