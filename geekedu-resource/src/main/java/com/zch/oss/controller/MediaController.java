package com.zch.oss.controller;

import com.zch.api.vo.resources.VideoPlayVO;
import com.zch.api.vo.resources.VideoVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.oss.service.IMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
