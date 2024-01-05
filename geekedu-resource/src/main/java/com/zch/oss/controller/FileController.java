package com.zch.oss.controller;

import com.zch.oss.domain.dto.FileDTO;
import com.zch.oss.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Poison02
 * @date 2024/1/5
 */
@RestController
@RequestMapping("/api/file")
@Api(tags = "文件资源管理接口")
@RequiredArgsConstructor
public class FileController {

    private final IFileService fileService;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public FileDTO uploadFile(@ApiParam(value = "文件数据") @RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

}
