package com.zch.oss.controller;

import com.zch.oss.domain.dto.FileDTO;
import com.zch.oss.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @ApiOperation("获取文件信息")
    @GetMapping("/getFIle/{id}")
    public FileDTO getFileInfo(@ApiParam(value = "文件id", example = "1") @PathVariable("id") Long id) {
        return fileService.getFileInfo(id);
    }

    @ApiOperation("删除文件信息")
    @GetMapping("/delete/{id}")
    public FileDTO deleteFileInfo(@ApiParam(value = "文件id", example = "1") @PathVariable("id") Long id) {
        return fileService.deleteFileInfo(id);
    }

}
