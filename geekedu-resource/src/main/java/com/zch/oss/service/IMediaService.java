package com.zch.oss.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.resource.BatchDelVideoForm;
import com.zch.api.dto.resource.VideoAddForm;
import com.zch.api.vo.resources.VideoPlayVO;
import com.zch.api.vo.resources.VideoVO;
import com.zch.oss.domain.po.Media;
import com.zch.oss.domain.vo.DogeCloudUploadVO;

/**
 * @author Poison02
 * @date 2024/1/4
 */
public interface IMediaService extends IService<Media> {

    /**
     * 获取预览播放视频的签名
     * @param mediaId
     * @return
     */
    VideoPlayVO getPreviewSignatureByMediaId(Long mediaId);

    /**
     * 分页查找视频列表
     * @param pageNum
     * @param pageSize
     * @param keywords
     * @return
     */
    Page<VideoVO> getVideoPage(Integer pageNum, Integer pageSize, String keywords);

    /**
     * 获取上传签名
     * @return
     */
    String getUploadSignature();

    /**
     * 新增视频
     * @param form
     * @return
     */
    VideoVO saveVideo(VideoAddForm form);

    /**
     * 批量删除视频
     * @param form
     * @return
     */
    Boolean deleteVideo(BatchDelVideoForm form);

    String getPlayUrl(Integer id);

    /**
     * 多吉云上传签名获取
     * @param filename
     * @return
     */
    DogeCloudUploadVO getDogeCloudUploadSignature(String filename);

}
