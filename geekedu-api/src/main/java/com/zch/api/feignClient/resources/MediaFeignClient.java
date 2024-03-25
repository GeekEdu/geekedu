package com.zch.api.feignClient.resources;

import com.zch.api.interceptor.FeignInterceptor;
import com.zch.api.vo.resources.AttachVO;
import com.zch.api.vo.resources.FileUploadVO;
import com.zch.common.mvc.result.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/6
 */
@FeignClient(contextId = "resource-service", value = "resource-service", configuration = FeignInterceptor.class)
public interface MediaFeignClient {

    /**
     * 获取视频真实播放地址
     * @param id
     * @return
     */
    @GetMapping("/api/media/video/{id}/playUrl")
    Response<String> getVideoPlayUrl(@PathVariable("id") Integer id);

    /**
     * 获取推流地址
     * @return
     */
    @GetMapping("/api/live/push/url")
    Response<String> getPushUrl();

    /**
     * 获取播放地址
     * @return
     */
    @GetMapping("/api/live/play/url")
    Response<String> getPlayUrl();

    /**
     * 上传图片
     * @param file
     * @param from
     * @return
     */
    @PostMapping(value = "/api/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response<FileUploadVO> uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("from") Integer from);

    /**
     * 获取课程附件列表
     * @param courseId
     * @return
     */
    @GetMapping("/api/attach/list")
    Response<List<AttachVO>> queryAttachList(@RequestParam("id") Integer courseId);

}
