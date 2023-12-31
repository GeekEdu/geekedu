package com.zch.media.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.media.domain.dto.MediaDTO;
import com.zch.media.domain.dto.MediaUploadResultDTO;
import com.zch.media.domain.query.MediaQuery;
import com.zch.media.domain.vo.MediaVO;
import com.zch.media.domain.vo.VideoPlayVO;
import com.zch.media.mapper.MediaMapper;
import com.zch.oss.domain.po.Media;

/**
 * @author Poison02
 * @date 2023/12/31
 */
public interface IMediaService extends IService<Media> {

    /**
     * 获得上传文件签名信息
     * @return
     */
    String getUploadSignature();

    /**
     * 通过章节id 获得播放视频签名信息
     * @param fileId 文件id
     * @return
     */
    VideoPlayVO getPlaySignatureBySectionId(Long fileId);

    /**
     * 保存视频信息
     * @param mediaResult
     * @return
     */
    MediaDTO save(MediaUploadResultDTO mediaResult);

    /**
     * 更新视频任务流
     * @param media
     */
    void updateMediaProcedureResult(Media media);

    /**
     * 删除视频
     * @param mediaId
     */
    void deleteMedia(String mediaId);

    /**
     * 通过视频id获得视频播放签名信息
     * @param mediaId
     * @return
     */
    VideoPlayVO getPlaySignatureByMediaId(Long mediaId);

    /**
     * 分页查询视频信息
     * @param query
     * @return
     */
    PageDTO<MediaVO> queryMediaPage(MediaQuery query);

}
