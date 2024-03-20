package com.zch.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.book.domain.po.LearnRecord;
import com.zch.book.mapper.LearnRecordMapper;
import com.zch.book.service.ILearnRecordService;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/20
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LearnRecordServiceImpl extends ServiceImpl<LearnRecordMapper, LearnRecord> implements ILearnRecordService {

    @Override
    public Boolean updateLearnRecord(Integer bookId, Integer articleId, Integer topicId, Long userId, String type) {
        LearnRecord record = new LearnRecord();
        if (StringUtils.isNotBlank(type) && "BOOK".equals(type)) {
            // 更新电子书学习记录
            if (ObjectUtils.isNotNull(bookId) && ObjectUtils.isNotNull(articleId)) {
                record.setBookId(bookId);
                record.setArticleId(articleId);
                record.setUserId(userId);
                record.setType(type);
                record.setLatestViewTime(LocalDateTime.now());
                save(record);
                return true;
            }
        } else if (StringUtils.isNotBlank(type) && "TOPIC".equals(type)) {
            // 更新图文学习记录
            if (ObjectUtils.isNotNull(topicId)) {
                record.setBookId(bookId);
                record.setUserId(userId);
                record.setType(type);
                record.setLatestViewTime(LocalDateTime.now());
                save(record);
                return true;
            }
        }
        return false;
    }

}
