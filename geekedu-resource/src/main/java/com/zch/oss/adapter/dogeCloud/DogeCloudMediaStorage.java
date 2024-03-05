package com.zch.oss.adapter.dogeCloud;

import com.zch.oss.adapter.MediaStorageAdapter;
import com.zch.oss.adapter.MediaUploadResult;
import com.zch.oss.domain.po.Media;

import java.io.InputStream;
import java.util.List;

/**
 * 多吉云 视频云上传
 * @author Poison02
 * @date 2024/3/5
 */
public class DogeCloudMediaStorage implements MediaStorageAdapter {
    @Override
    public String getUploadSignature() {
        return null;
    }

    @Override
    public String getPlaySignature(String mediaId, Long userId, Integer freeExpire) {
        return null;
    }

    @Override
    public MediaUploadResult uploadFile(String mediaName, InputStream inputStream, long contentLength) {
        return null;
    }

    @Override
    public void deleteFile(String mediaId) {

    }

    @Override
    public void deleteFiles(List<String> mediaIds) {

    }

    @Override
    public List<Media> queryMediaInfos(String... mediaIds) {
        return null;
    }
}
