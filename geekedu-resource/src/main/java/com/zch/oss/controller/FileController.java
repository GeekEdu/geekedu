package com.zch.oss.controller;

import com.zch.api.vo.resources.FileUploadVO;
import com.zch.api.vo.resources.FileVO;
import com.zch.common.mvc.result.Response;
import com.zch.oss.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Poison02
 * @date 2024/1/5
 */
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final IFileService fileService;

    @PostMapping("/upload")
    public Response<FileUploadVO> uploadFile(@RequestParam("file") MultipartFile file) {
        return Response.success(fileService.uploadFile(file));
    }

    @GetMapping("/getFile/{id}")
    public Response<FileVO> getFileInfo(@PathVariable("id") Long id) {
        return Response.success(fileService.getFileInfo(id));
    }

    @GetMapping("/delete/{id}")
    public Response<Boolean> deleteFileInfo(@PathVariable("id") Long id) {
        return Response.judge(fileService.deleteFileInfo(id));
    }

}
