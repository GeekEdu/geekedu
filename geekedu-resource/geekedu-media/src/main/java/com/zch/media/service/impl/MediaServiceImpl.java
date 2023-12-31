package com.zch.media.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.media.domain.dto.MediaDTO;
import com.zch.media.domain.dto.MediaUploadResultDTO;
import com.zch.media.domain.query.MediaQuery;
import com.zch.media.domain.vo.MediaVO;
import com.zch.media.domain.vo.VideoPlayVO;
import com.zch.media.mapper.MediaMapper;
import com.zch.media.service.IMediaService;
import com.zch.oss.adapter.MediaStorageAdapter;
import com.zch.oss.domain.po.Media;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@Service
@RequiredArgsConstructor
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements IMediaService {

    private final MediaStorageAdapter mediaStorage;

    @Override
    public String getUploadSignature() {
        return mediaStorage.getUploadSignature();
    }

    @Override
    public VideoPlayVO getPlaySignatureBySectionId(Long fileId) {
        return null;
    }

    @Override
    public MediaDTO save(MediaUploadResultDTO mediaResult) {
        return null;
    }

    @Override
    public void updateMediaProcedureResult(Media media) {

    }

    @Override
    public void deleteMedia(String fileId) {

    }

    @Override
    public VideoPlayVO getPlaySignatureByMediaId(Long mediaId) {
        return null;
    }

    @Override
    public PageDTO<MediaVO> queryMediaPage(MediaQuery query) {
        return null;
    }
}
