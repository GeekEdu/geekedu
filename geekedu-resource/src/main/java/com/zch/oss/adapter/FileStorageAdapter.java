package com.zch.oss.adapter;

import java.io.InputStream;
import java.util.List;

/**
 * 文件存储适配器
 * @author Poison02
 * @date 2023/12/22
 */
public interface FileStorageAdapter {

    /**
     * 上传文件
     * @param key 文件唯一标识 a.jpg
     * @param inputStream 文件流
     * @param contentLength
     * @return requestId 请求ID
     */
    String uploadFile(String key, InputStream inputStream, long contentLength);

    /**
     * 下载文件
     * @param key 文件唯一标识 a.jpg
     * @return 文件流
     */
    InputStream downloadFile(String key);

    /**
     * 删除指定文件
     * @param key 文件唯一标识 a.jpg
     */
    void deleteFile(String key);

    /**
     * 删除文件集合
     * @param keys 文件唯一标识 a.jpg 的集合
     */
    void deleteFiles(List<String> keys);

}
