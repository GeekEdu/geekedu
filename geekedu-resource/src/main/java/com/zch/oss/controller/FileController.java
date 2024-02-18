package com.zch.oss.controller;

import com.zch.api.vo.resources.FileUploadVO;
import com.zch.api.vo.resources.FileVO;
import com.zch.api.vo.resources.ImageListVO;
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

    /**
     * 分页获取图片资源列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/images")
    public Response<ImageListVO> getImages(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        return Response.success(fileService.getImagesList(pageNum, pageSize));
    }

    /**
     * 上传图片
     * @param file
     * @param from
     * @return
     */
    @PostMapping("/upload")
    public Response<FileUploadVO> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("from") Integer from) {
        return Response.success(fileService.uploadFile(file, from));
    }

    /**
     * 根据id查看图片明细
     * @param id
     * @return
     */
    @GetMapping("/getFile/{id}")
    public Response<FileVO> getFileInfo(@PathVariable("id") Long id) {
        return Response.success(fileService.getFileInfo(id));
    }

    /**
     * 根据id删除图片
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public Response<Boolean> deleteFileInfo(@PathVariable("id") Long id) {
        return Response.judge(fileService.deleteFileInfo(id));
    }

}
