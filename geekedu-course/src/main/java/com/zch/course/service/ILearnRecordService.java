package com.zch.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.course.domain.po.LearnRecord;

/**
 * @author Poison02
 * @date 2024/3/20
 */
public interface ILearnRecordService extends IService<LearnRecord> {

    /**
     * 更新学习记录
     * @param courseId
     * @param videoId
     * @param duration
     * @param userId
     * @param type
     * @return
     */
    Boolean updateLearnRecord(Integer courseId, Integer videoId, Integer duration, Long userId, String type);

}
