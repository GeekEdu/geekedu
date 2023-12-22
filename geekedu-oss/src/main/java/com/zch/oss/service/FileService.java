package com.zch.oss.service;

import com.zch.oss.adapter.StorageAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Poison02
 * @date 2023/12/22
 */
@Service
public class FileService {

    private final StorageAdapter storageAdapter;

    public FileService(StorageAdapter storageAdapter) {
        this.storageAdapter = storageAdapter;
    }

    /**
     * 列出所有桶
     * @return
     */
    public List<String> getAllBucket() {
        return storageAdapter.getAllBucket();
    }

    /**
     * 获取文件URL
     * @param bucketName
     * @param objectName
     * @return
     */
    public String getUrl(String bucketName, String objectName) {
        return storageAdapter.getUrl(bucketName, objectName);
    }

    /**
     * 上传文件
     * @param uploadFile
     * @param bucket
     * @param objectName
     * @return
     */
    public String uploadFile(MultipartFile uploadFile, String bucket, String objectName) {
        storageAdapter.uploadFile(uploadFile, bucket, objectName);
        objectName = objectName + "/" + uploadFile.getOriginalFilename();
        return storageAdapter.getUrl(bucket, objectName);
    }

}
