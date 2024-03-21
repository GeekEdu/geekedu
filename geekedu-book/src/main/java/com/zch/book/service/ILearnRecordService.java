package com.zch.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.book.domain.po.LearnRecord;

import java.util.List;

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

    /**
     * 查找学习记录列表
     * @param userId
     * @param type
     * @return
     */
    List<LearnRecord> queryLearnRecord(Long userId, String type);

}
