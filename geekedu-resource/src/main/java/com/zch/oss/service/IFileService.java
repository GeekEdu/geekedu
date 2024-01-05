package com.zch.oss.service;

import com.zch.oss.domain.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Poison02
 * @date 2024/1/4
 */
// public interface IFileService extends IService<File> {
public interface IFileService{

    /**
     * 上传文件
     * @param file 文件
     * @return
     */
    FileDTO uploadFile(MultipartFile file);

    /**
     * 获取文件信息
     * @param id 文件id
     * @return
     */
    FileDTO getFileInfo(Long id);

}
