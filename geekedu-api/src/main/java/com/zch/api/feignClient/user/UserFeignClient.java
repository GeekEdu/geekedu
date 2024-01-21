package com.zch.api.feignClient.user;

import com.zch.api.vo.user.CaptchaVO;
import com.zch.common.mvc.result.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Poison02
 * @date 2024/1/20
 */
@FeignClient(contextId = "user", name = "user-service")
public interface UserFeignClient {

    @GetMapping("/api/captcha/image")
    Response<CaptchaVO> getCaptcha();

}
