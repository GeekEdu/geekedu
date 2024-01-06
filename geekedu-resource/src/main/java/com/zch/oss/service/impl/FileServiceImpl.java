package com.zch.oss.service.impl;

import cn.hutool.core.lang.UUID;
import com.zch.common.exceptions.CommonException;
import com.zch.common.exceptions.DbException;
import com.zch.common.utils.StringUtils;
import com.zch.oss.adapter.FileStorageAdapter;
import com.zch.oss.config.properties.PlatformProperties;
import com.zch.oss.domain.dto.FileDTO;
import com.zch.oss.domain.po.File;
import com.zch.oss.enums.FileErrorInfo;
import com.zch.oss.enums.FilePlatform;
import com.zch.oss.enums.FileStatus;
import com.zch.oss.mapper.FileMapper;
import com.zch.oss.service.IFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Poison02
 * @date 2024/1/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
// public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {
public class FileServiceImpl implements IFileService {

    private final FileStorageAdapter fileStorageAdapter;

    private final PlatformProperties properties;

    private final FileMapper fileMapper;

    @Override
    public FileDTO uploadFile(MultipartFile file) {
        // 1. 获取文件名称
        String originalFileName = file.getOriginalFilename();
        // 2. 生成新文件名
        String newFileName = generateNewFileName(originalFileName);
        // 3. 获取文件流
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new CommonException("上传文件时文件读取异常", e);
        }
        // 4. 上传文件
        String requestId = fileStorageAdapter.uploadFile(newFileName, inputStream, file.getSize());
        // 5. 写入数据库
        File fileInfo = null;
        try {
            fileInfo = new File();
            fileInfo.setFileName(originalFileName);
            fileInfo.setKeyId(newFileName);
            fileInfo.setStatus(FileStatus.UPLOADED.getValue());
            fileInfo.setRequestId(requestId);
            fileInfo.setPlatform(properties.getFile().getValue());
            // save(fileInfo);
            fileMapper.insertFileInfo(fileInfo);
        } catch (Exception e) {
            log.error("文件信息保存异常", e);
            fileStorageAdapter.deleteFile(newFileName);
            throw new DbException(FileErrorInfo.Msg.FILE_UPLOAD_ERROR);
        }
        // 6. 返回
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(fileInfo.getId());
        fileDTO.setPath(FilePlatform.returnPath(fileInfo.getPlatform()) + newFileName);
        fileDTO.setFileName(originalFileName);
        return fileDTO;
    }

    @Override
    public FileDTO getFileInfo(Long id) {
        return null;
    }

    /**
     * 生成新文件名
     * @param originalFileName 原始文件名
     * @return
     */
    private String generateNewFileName(String originalFileName) {
        // 2. 获取后缀
        String suffix = StringUtils.subAfter(originalFileName, ".", true);
        // 2. 生成新文件名
        return UUID.randomUUID().toString(true) + "." + suffix;
    }
}
