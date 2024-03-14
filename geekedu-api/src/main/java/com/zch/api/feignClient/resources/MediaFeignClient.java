package com.zch.api.feignClient.resources;

import com.zch.api.interceptor.FeignInterceptor;
import com.zch.common.mvc.result.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public Response<String> getVideoPlayUrl(@PathVariable("id") Integer id);

    /**
     * 获取推流地址
     * @return
     */
    @GetMapping("/api/live/push/url")
    public Response<String> getPushUrl();

    /**
     * 获取播放地址
     * @return
     */
    @GetMapping("/api/live/play/url")
    public Response<String> getPlayUrl();

}
