package com.zch.oss.controller;

import com.zch.api.dto.resource.BatchDelVideoForm;
import com.zch.api.dto.resource.VideoAddForm;
import com.zch.api.vo.resources.VideoPlayVO;
import com.zch.api.vo.resources.VideoVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.oss.service.IMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/2/18
 */
@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final IMediaService mediaService;

    /**
     * 根据视频在云端的视频id获取预览播放签名
     * @param mediaId
     * @return
     */
    @GetMapping("/signature/preview")
    public Response<VideoPlayVO> getPreviewSignature(@RequestParam("mediaId") Long mediaId) {
        return Response.success(mediaService.getPreviewSignatureByMediaId(mediaId));
    }

    /**
     * 分页返回视频列表
     * @param pageNum
     * @param pageSize
     * @param keywords
     * @return
     */
    @GetMapping("/getVideoPage")
    public PageResult<VideoVO> getVideoPage(@RequestParam("pageNum") Integer pageNum,
                                            @RequestParam("pageSize") Integer pageSize,
                                            @RequestParam("keywords") String keywords) {
        return PageResult.success(mediaService.getVideoPage(pageNum, pageSize, keywords));
    }

    /**
     * 获取上传视频签名
     * @return
     */
    @GetMapping("/getUploadSignature")
    public Response<String> getUploadSignature() {
        return Response.success(mediaService.getUploadSignature());
    }

    /**
     * 新增视频到数据库
     * @param form
     * @return
     */
    @PostMapping("/video/add")
    public Response<VideoVO> addVideo(@RequestBody VideoAddForm form) {
        return Response.success(mediaService.saveVideo(form));
    }

    @PostMapping("/video/delete")
    public Response<Boolean> deleteVideo(@RequestBody BatchDelVideoForm form) {
        return Response.success(mediaService.deleteVideo(form));
    }

    /**
     * 获取视频真实播放地址
     * @param id
     * @return
     */
    @GetMapping("/video/{id}/playUrl")
    public Response<String> getVideoPlayUrl(@PathVariable("id") Integer id) {
        return Response.success(mediaService.getPlayUrl(id));
    }

}
