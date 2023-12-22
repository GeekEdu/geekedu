package com.zch.oss.controller;

import com.zch.oss.entity.Result;
import com.zch.oss.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Poison02
 * @date 2023/12/22
 */
@RestController
@RequestMapping("/api/oss")
public class FileController {

    @Resource
    private FileService fileService;

    @GetMapping("/testGetAllBuckets")
    public String testGetAllBuckets() throws Exception {
        List<String> allBucket = fileService.getAllBucket();
        return allBucket.get(0);
    }

    @GetMapping("/getUrl")
    public String getUrl(String bucket, String objectName) throws Exception {
        return fileService.getUrl(bucket, objectName);
    }

    @PostMapping("/upload")
    public Result upload(MultipartFile uploadFile, String bucket, String objectName) throws Exception {
        String url = fileService.uploadFile(uploadFile, bucket, objectName);
        return Result.ok(url);
    }

}
