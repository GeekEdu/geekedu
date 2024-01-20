package com.zch.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.oss.domain.dto.MediaDTO;
import com.zch.oss.domain.dto.MediaUploadResultDTO;
import com.zch.oss.domain.po.Media;
import com.zch.oss.domain.vo.VideoPlayVO;

/**
 * @author Poison02
 * @date 2024/1/4
 */
public interface IMediaService extends IService<Media> {

    String getUploadSignature();

    VideoPlayVO getPlaySignatureBySectionId(Long mediaId);

    MediaDTO save(MediaUploadResultDTO mediaUploadResultDTO);

    void updateMediaProcedureResult(Media media);

    void deleteMedia(String mediaId);

    VideoPlayVO getPlaySignatureByMediaId(Long mediaId);

}
