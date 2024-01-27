package com.zch.oss.controller;

import com.zch.oss.domain.dto.FileDTO;
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
    public FileDTO uploadFile(@RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

    @GetMapping("/getFile/{id}")
    public FileDTO getFileInfo(@PathVariable("id") Long id) {
        return fileService.getFileInfo(id);
    }

    @GetMapping("/delete/{id}")
    public FileDTO deleteFileInfo(@PathVariable("id") Long id) {
        return fileService.deleteFileInfo(id);
    }

}
