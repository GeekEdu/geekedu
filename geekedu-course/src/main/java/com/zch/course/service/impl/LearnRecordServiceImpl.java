package com.zch.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.course.domain.po.LearnRecord;
import com.zch.course.mapper.LearnRecordMapper;
import com.zch.course.service.ILearnRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/3/20
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LearnRecordServiceImpl extends ServiceImpl<LearnRecordMapper, LearnRecord> implements ILearnRecordService {
    @Override
    public Boolean updateLearnRecord(Integer courseId, Integer videoId, Integer duration, Long userId, String type) {
        if (StringUtils.isNotBlank(type)) {
            LearnRecord record = getOne(new LambdaQueryWrapper<LearnRecord>()
                    .eq(LearnRecord::getCourseId, courseId)
                    .eq(LearnRecord::getVideoId, videoId));
            if (ObjectUtils.isNull(record)) {
                record.setCourseId(courseId);
                record.setVideoId(videoId);
                record.setDuration(duration);
                record.setUserId(userId);
                record.setType(type);
                save(record);
                return true;
            }
            record.setDuration(record.getDuration() + duration);
            updateById(record);
            return true;
        }
        return false;
    }
}
