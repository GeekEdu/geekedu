package com.zch.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.resource.BatchDelVideoForm;
import com.zch.api.dto.resource.VideoAddForm;
import com.zch.api.vo.resources.VideoPlayVO;
import com.zch.api.vo.resources.VideoVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.oss.adapter.MediaStorageAdapter;
import com.zch.oss.adapter.dogeCloud.DogeCloudMediaStorage;
import com.zch.oss.domain.po.Media;
import com.zch.oss.domain.vo.DogeCloudUploadVO;
import com.zch.oss.mapper.MediaMapper;
import com.zch.oss.service.IMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final DogeCloudMediaStorage dogeCloudMediaStorage;

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

    @Override
    public String getUploadSignature() {
        return mediaStorageAdapter.getUploadSignature();
    }

    @Override
    public VideoVO saveVideo(VideoAddForm form) {
        Media media = new Media();
        media.setMediaId(form.getMediaId());
        media.setCoverLink(form.getCoverLink());
        media.setMediaName(form.getMediaName());
        media.setDuration(form.getDuration());
        media.setSize(form.getSize());
        media.setMediaSource(form.getMediaSource());
        media.setSizeMb(form.getSize() / 1000000.00);
        mediaMapper.insert(media);
        VideoVO vo = new VideoVO();
        Media one = mediaMapper.selectOne(new LambdaQueryWrapper<Media>()
                .eq(Media::getIsDelete, 0)
                .eq(Media::getMediaId, form.getMediaId()));
        BeanUtils.copyProperties(one, vo);
        return vo;
    }

    @Transactional
    @Override
    public Boolean deleteVideo(BatchDelVideoForm form) {
        if (ObjectUtils.isNull(form.getIds()) || CollUtils.isEmpty(form.getIds())) {
            return false;
        }
        // 根据这些id查找出视频id
        List<Media> media = mediaMapper.selectBatchIds(form.getIds());
        List<String> ids = media.stream().map(Media::getMediaId).collect(Collectors.toList());
        // 删除云端的视频
        mediaStorageAdapter.deleteFiles(ids);
        // 删除数据库的视频
        return removeBatchByIds(form.getIds());
    }

    @Override
    public DogeCloudUploadVO getDogeCloudUploadSignature(String filename) {
        if (StringUtils.isBlank(filename)) {
            return null;
        }
        DogeCloudUploadVO vo = null;
        try {
            vo = dogeCloudMediaStorage.getUploadSignature(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }
}
