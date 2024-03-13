package com.zch.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.course.live.LiveVideoForm;
import com.zch.api.vo.course.live.LiveVideoVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.course.domain.po.LiveVideo;
import com.zch.course.mapper.LiveVideoMapper;
import com.zch.course.service.ILiveChapterService;
import com.zch.course.service.ILiveVideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LiveVideoServiceImpl extends ServiceImpl<LiveVideoMapper, LiveVideo> implements ILiveVideoService {

    private final ILiveChapterService chapterService;

    @Override
    public Page<LiveVideoVO> getVideoList(Integer pageNum, Integer pageSize, Integer courseId) {
        Page<LiveVideo> page = page(new Page<LiveVideo>(pageNum, pageSize), new LambdaQueryWrapper<LiveVideo>()
                .eq(LiveVideo::getCourseId, courseId));
        Page<LiveVideoVO> vo = new Page<>();
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
        }
        vo.setRecords(page.getRecords().stream().map(item -> {
            LiveVideoVO vo1 = new LiveVideoVO();
            BeanUtils.copyProperties(item, vo1);
            vo1.setChapter(chapterService.getChapterById(item.getChapterId()));
            vo1.setStatusText(item.getStatus().getValue());
            return vo1;
        }).collect(Collectors.toList()));
        vo.setTotal(page.getTotal());
        return vo;
    }

    @Override
    public LiveVideoVO getVideoDetail(Integer id) {
        LiveVideo video = getById(id);
        if (ObjectUtils.isNull(video)) {
            return null;
        }
        LiveVideoVO vo = new LiveVideoVO();
        BeanUtils.copyProperties(video, vo);
        return vo;
    }

    @Override
    public Boolean updateVideo(Integer id, LiveVideoForm form) {
        LiveVideo video = getById(id);
        if (ObjectUtils.isNull(video)) {
            return false;
        }
        video.setTitle(form.getTitle());
        video.setCourseId(form.getCourseId());
        video.setChapterId(form.getChapterId());
        video.setIsShow(form.getIsShow());
        video.setLiveTime(form.getLiveTime());
        video.setEstimateDuration(form.getEstimateDuration());
        return updateById(video);
    }

    @Override
    public Boolean deleteVideo(Integer id) {
        return removeById(id);
    }

    @Override
    public Boolean addVideo(LiveVideoForm form) {
        LiveVideo video = new LiveVideo();
        video.setTitle(form.getTitle());
        video.setCourseId(form.getCourseId());
        video.setChapterId(form.getChapterId());
        video.setIsShow(form.getIsShow());
        video.setLiveTime(form.getLiveTime());
        video.setEstimateDuration(form.getEstimateDuration());
        return save(video);
    }

    @Override
    public List<LiveVideoVO> getVideoList(Integer courseId) {
        List<LiveVideo> list = list(new LambdaQueryWrapper<LiveVideo>().eq(LiveVideo::getCourseId, courseId));
        if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        return list.stream().map(item -> {
            LiveVideoVO vo = new LiveVideoVO();
            BeanUtils.copyProperties(item, vo);
            vo.setChapter(chapterService.getChapterById(item.getChapterId()));
            vo.setStatusText(item.getStatus().getValue());
            return vo;
        }).collect(Collectors.toList());
    }
}
