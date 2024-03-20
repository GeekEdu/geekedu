package com.zch.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.book.domain.po.LearnRecord;

/**
 * @author Poison02
 * @date 2024/3/20
 */
public interface ILearnRecordService extends IService<LearnRecord> {

    /**
     * 更新学习记录
     * @param bookId
     * @param articleId
     * @param topicId
     * @param userId
     * @param type
     * @return
     * */
    Boolean updateLearnRecord(Integer bookId, Integer articleId, Integer topicId, Long userId, String type);

}
