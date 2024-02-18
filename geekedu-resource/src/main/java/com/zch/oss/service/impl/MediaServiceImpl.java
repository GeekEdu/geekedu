package com.zch.oss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.resources.VideoPlayVO;
import com.zch.common.satoken.context.UserContext;
import com.zch.oss.adapter.MediaStorageAdapter;
import com.zch.oss.domain.po.Media;
import com.zch.oss.mapper.MediaMapper;
import com.zch.oss.service.IMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
