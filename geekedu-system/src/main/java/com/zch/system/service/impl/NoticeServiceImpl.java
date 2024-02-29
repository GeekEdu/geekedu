package com.zch.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.system.notice.NoticeVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.system.domain.po.Notice;
import com.zch.system.mapper.NoticeMapper;
import com.zch.system.service.INoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/29
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public List<NoticeVO> getNoticeList() {
        List<Notice> notices = noticeMapper.selectList(new LambdaQueryWrapper<Notice>());
        if (ObjectUtils.isNull(notices) || CollUtils.isEmpty(notices)) {
            return new ArrayList<>(0);
        }
        List<NoticeVO> vos = notices.stream().map(item -> {
            NoticeVO vo = new NoticeVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public NoticeVO getLatestNotice() {
        long count = count();
        if (count == 0) {
            return null;
        }
        Notice notice = noticeMapper.selectOne(new LambdaQueryWrapper<Notice>()
                .last(" limit " + (count - 1) + ", 1"));
        if (ObjectUtils.isNull(notice)) {
            return null;
        }
        NoticeVO vo = new NoticeVO();
        BeanUtils.copyProperties(notice, vo);
        return vo;
    }

    @Override
    public NoticeVO getNoticeById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return null;
        }
        Notice notice = noticeMapper.selectById(id);
        if (ObjectUtils.isNull(notice)) {
            return null;
        }
        NoticeVO vo = new NoticeVO();
        BeanUtils.copyProperties(notice, vo);
        return vo;
    }
}
