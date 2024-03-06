package com.zch.oss.adapter;

import com.zch.oss.domain.po.Media;

import java.io.InputStream;
import java.util.List;

/**
 * @author Poison02
 * @date 2023/12/31
 */
public interface MediaStorageAdapter {

    /**
     * 获取临时上传的授权签名
     * @return 签名信息
     */
    String getUploadSignature();

    /**
     * 获取播放的授权签名
     * @param mediaId 视频文件id
     * @param userId 查看视频的用户id，用于生成水印
     * @param freeExpire 免费观看时长，null表示不限制时长
     * @return 签名信息
     */
    String getPlaySignature(String mediaId, Long userId, Integer freeExpire);

    /**
     * 上传文件
     * @param mediaName 视频文件名称 a.mp4
     * @param inputStream 文件流
     * @param contentLength
     * @return requestId
     */
    MediaUploadResult uploadFile(String mediaName, InputStream inputStream, long contentLength);

    /**
     * 删除指定文件
     * @param mediaId 视频文件id
     */
    void deleteFile(String mediaId);

    /**
     * 删除文件集合
     * @param mediaIds 视频文件id集合
     */
    void deleteFiles(List<String> mediaIds);

    /**
     * 根绝 mediaId 查询视频信息
     * @param mediaIds 视频文件id集合
     * @return 视频信息列表啊
     */
    List<Media> queryMediaInfos(String ... mediaIds);

    /**
     * 获取播放URL
     * @param mediaId
     * @return
     */
    String getPlayUrl(String mediaId);

}
