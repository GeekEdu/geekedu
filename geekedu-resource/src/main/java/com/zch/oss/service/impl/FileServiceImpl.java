package com.zch.oss.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.resources.*;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.exception.CommonException;
import com.zch.common.mvc.exception.DbException;
import com.zch.common.satoken.context.UserContext;
import com.zch.oss.adapter.FileStorageAdapter;
import com.zch.oss.config.properties.PlatformProperties;
import com.zch.oss.domain.po.File;
import com.zch.oss.enums.FileErrorInfo;
import com.zch.oss.enums.FileFromEnum;
import com.zch.oss.enums.FilePathEnum;
import com.zch.oss.mapper.FileMapper;
import com.zch.oss.service.IFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zch.oss.constants.FileErrorInfo.FILE_NOT_EXISTS;

/**
 * @author Poison02
 * @date 2024/1/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    private final FileStorageAdapter fileStorageAdapter;

    private final PlatformProperties properties;

    private final FileMapper fileMapper;

    // cos链接
    private static final String TENCENT_LINK = "https://geekedu-1315662121.cos.ap-chengdu.myqcloud.com";

    @Override
    public ImageListVO getImagesList(Integer pageNum, Integer pageSize) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)) {
            pageNum = 0;
            pageSize = 10;
        }
        // 构造返回值
        ImageListVO vo = new ImageListVO();
        long count = count();
        // 没有图片则直接返回
        if (count == 0) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.setFrom(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        Page<File> page = page(new Page<>(pageNum, pageSize), wrapper);
        List<File> files = page.getRecords();
        if (CollUtils.isEmpty(files)) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.setFrom(new ArrayList<>(0));
            return vo;
        }
        List<ImageVO> imageVOS = new ArrayList<>(files.size());
        for (File file : files) {
            ImageVO imageVO = new ImageVO();
            imageVO.setId(file.getId());
            imageVO.setFrom(file.getFileFrom().getCode());
            imageVO.setUrl(TENCENT_LINK + file.getKeyId());
            imageVO.setPath(file.getFilePath());
            imageVO.setSource(file.getPlatform().getDesc());
            imageVO.setName(file.getFileName());
            imageVO.setCreatedTime(file.getCreatedTime());
            imageVOS.add(imageVO);
        }
        vo.getData().setTotal(count);
        vo.getData().setData(imageVOS);
        // 查询所有种类的图片来源
        List<File> list = fileMapper.selectList(new LambdaQueryWrapper<File>()
                .eq(File::getIsDelete, 0)
                .select(File::getFileFrom)
                .groupBy(File::getFileFrom));
        List<FileFromVO> fromVOS = list.stream().map(item -> {
            FileFromVO fromVO = new FileFromVO();
            fromVO.setKey(item.getFileFrom().getCode());
            fromVO.setName(item.getFileFrom().getValue());
            return fromVO;
        }).collect(Collectors.toList());
        vo.setFrom(fromVOS);
        return vo;
    }

    @Override
    public FileUploadVO uploadFile(MultipartFile file, Integer from) {
        // 1. 获取文件名称
        String originalFileName = file.getOriginalFilename();
        // 2. 生成新文件名
        String newFileName = generateNewFileName(originalFileName, from);
        // 3. 获取文件流
        InputStream inputStream;
        Long userId = UserContext.getLoginId();
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
            fileInfo.setFileFrom(FileFromEnum.fromCode(0));
            fileInfo.setFilePath(newFileName);
            fileInfo.setRequestId(requestId);
            fileInfo.setPlatform(properties.getFile());
            fileInfo.setCreatedBy(userId);
            fileInfo.setUpdatedBy(userId);
            fileMapper.insert(fileInfo);
        } catch (Exception e) {
            log.error("文件信息保存异常", e);
            fileStorageAdapter.deleteFile(newFileName);
            throw new DbException(FileErrorInfo.Msg.FILE_UPLOAD_ERROR);
        }
        // 6. 返回
        FileUploadVO vo = new FileUploadVO();
        vo.setId(fileInfo.getId());
        vo.setRequestId(requestId);
        vo.setKeyId(newFileName);
        vo.setPlatform("cos");
        vo.setLink(TENCENT_LINK + newFileName);
        return vo;
    }

    @Override
    public FileVO getFileInfo(Long id) {
        File file = fileMapper.selectById(id);
        if (file == null) {
            throw new DbException(FILE_NOT_EXISTS);
        }
        FileVO vo = new FileVO();
        vo.setId(file.getId());
        vo.setFileName(file.getKeyId());
        vo.setLink(TENCENT_LINK + file.getKeyId());
        return vo;
    }

    @Override
    public Boolean deleteFileInfo(Long id) {
        int row = fileMapper.deleteById(id);
        return row == 1;
    }

    /**
     * 生成新文件名
     * @param originalFileName 原始文件名
     * @param from 图片上传来源
     * @return
     */
    private String generateNewFileName(String originalFileName, Integer from) {
        // 1. 获取后缀
        String suffix = StringUtils.subAfter(originalFileName, ".", true);
        // 2. 获取上传来源，得到路径
        String path = FilePathEnum.returnPath(from);
        // 3. 和路径拼接生成新文件名
        return path + UUID.randomUUID().toString(true) + "." + suffix;
    }
}
