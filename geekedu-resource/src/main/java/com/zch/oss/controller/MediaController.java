package com.zch.oss.controller;

import com.zch.api.vo.resources.VideoPlayVO;
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

    @GetMapping("/signature/preview")
    public Response<VideoPlayVO> getPreviewSignature(@RequestParam("mediaId") Long mediaId) {
        return Response.success(mediaService.getPreviewSignatureByMediaId(mediaId));
    }

}
