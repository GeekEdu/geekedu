package com.zch.oss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.oss.adapter.MediaStorageAdapter;
import com.zch.oss.domain.dto.MediaDTO;
import com.zch.oss.domain.dto.MediaUploadResultDTO;
import com.zch.oss.domain.po.Media;
import com.zch.oss.domain.vo.VideoPlayVO;
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
    public String getUploadSignature() {
        return mediaStorageAdapter.getUploadSignature();
    }

    @Override
    public VideoPlayVO getPlaySignatureBySectionId(Long mediaId) {
        return null;
    }

    @Override
    public MediaDTO save(MediaUploadResultDTO mediaUploadResultDTO) {
        return null;
    }

    @Override
    public void updateMediaProcedureResult(Media media) {

    }

    @Override
    public void deleteMedia(String mediaId) {

    }

    @Override
    public VideoPlayVO getPlaySignatureByMediaId(Long mediaId) {
        return null;
    }
}
