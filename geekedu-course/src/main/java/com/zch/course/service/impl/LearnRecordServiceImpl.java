package com.zch.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.course.record.LearnRecordVO;
import com.zch.common.core.utils.BeanUtils;
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
                    .eq(LearnRecord::getVideoId, videoId)
                    .eq(LearnRecord::getUserId, userId)
                    .eq(LearnRecord::getType, type));
            if (ObjectUtils.isNull(record)) {
                LearnRecord record1 = new LearnRecord();
                record1.setCourseId(courseId);
                record1.setVideoId(videoId);
                record1.setDuration(duration);
                record1.setUserId(userId);
                record1.setType(type);
                save(record1);
                return true;
            }
            record.setDuration(duration);
            updateById(record);
            return true;
        }
        return false;
    }

    @Override
    public LearnRecordVO getLearnRecord(Integer courseId, Integer videoId, Long userId, String type) {
        LearnRecord record = getOne(new LambdaQueryWrapper<LearnRecord>()
                .eq(LearnRecord::getCourseId, courseId)
                .eq(LearnRecord::getVideoId, videoId)
                .eq(LearnRecord::getUserId, userId)
                .eq(LearnRecord::getType, type));
        if (ObjectUtils.isNull(record)) {
            return null;
        }
        LearnRecordVO vo = new LearnRecordVO();
        BeanUtils.copyProperties(record, vo);
        return vo;
    }
}
