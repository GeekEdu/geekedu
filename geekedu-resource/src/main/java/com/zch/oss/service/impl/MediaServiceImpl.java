package com.zch.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.resources.VideoPlayVO;
import com.zch.api.vo.resources.VideoVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.oss.adapter.MediaStorageAdapter;
import com.zch.oss.domain.po.Media;
import com.zch.oss.mapper.MediaMapper;
import com.zch.oss.service.IMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/1/20
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements IMediaService {

    private final MediaMapper mediaMapper;

    private final MediaStorageAdapter mediaStorageAdapter;

    @Override
    public VideoPlayVO getPreviewSignatureByMediaId(Long mediaId) {
        // 1. 根据id查询视频信息
        Media media = mediaMapper.selectById(mediaId);
        // 2. 获取签名
        String signature = mediaStorageAdapter.getPlaySignature(media.getMediaId(), UserContext.getLoginId(), null);
        // 3. 构造数据返回
        VideoPlayVO vo = new VideoPlayVO();
        vo.setMediaId(media.getMediaId());
        vo.setSignature(signature);
        return vo;
    }

    @Override
    public Page<VideoVO> getVideoPage(Integer pageNum, Integer pageSize, String keywords) {
        if (Objects.isNull(pageNum) || Objects.isNull(pageSize)) {
            pageNum = 0;
            pageSize = 10;
        }
        Page<Media> mediaPage = null;
        if (StringUtils.isBlank(keywords)) {
            mediaPage = mediaMapper.selectPage(new Page<>(pageNum, pageSize),
                    new LambdaQueryWrapper<Media>()
                            .eq(Media::getIsDelete, 0));
        } else {
            mediaPage = mediaMapper.selectPage(new Page<>(pageNum, pageSize),
                    new LambdaQueryWrapper<Media>()
                            .eq(Media::getIsDelete, 0)
                            .like(Media::getMediaName, keywords));
        }
        Page<VideoVO> vo = new Page<>();
        if (CollUtils.isNotEmpty(mediaPage.getRecords())) {
            List<VideoVO> vos = mediaPage.getRecords().stream().map(item -> {
                VideoVO vo1 = new VideoVO();
                BeanUtils.copyProperties(item, vo1);
                return vo1;
            }).collect(Collectors.toList());
            vo.setRecords(vos);
            vo.setTotal(mediaPage.getTotal());
        }
        return vo;
    }
}
