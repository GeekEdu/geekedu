package com.zch.oss.adapter;

import com.zch.oss.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 文件存储适配器
 * @author Poison02
 * @date 2023/12/22
 */
public interface StorageAdapter {

    /**
     * 创建桶
     * @param bucket
     */
    void createBucket(String bucket);

    /**
     * 上传文件
     * @param uploadFile
     * @param bucket
     * @param objectName
     */
    void uploadFile(MultipartFile uploadFile, String bucket, String objectName);

    /**
     * 列出所有桶
     * @return
     */
    List<String> getAllBucket();

    /**
     * 列出当前桶和文件
     * @param bucket
     * @return
     */
    List<FileInfo> getAllFile(String bucket);

    /**
     * 下载文件
     * @param bucket
     * @param objectName
     * @return
     */
    InputStream downLoad(String bucket, String objectName);

    /**
     * 删除桶
     * @param bucket
     */
    void deleteBucket(String bucket);

    /**
     * 删除文件
     * @param bucket
     * @param objectName
     */
    void deleteObject(String bucket, String objectName);

    String getUrl(String bucket, String objectName);

}
