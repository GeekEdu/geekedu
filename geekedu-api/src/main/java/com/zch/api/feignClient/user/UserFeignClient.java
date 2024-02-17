package com.zch.api.feignClient.user;

import com.zch.api.interceptor.FeignInterceptor;
import com.zch.api.vo.user.CaptchaVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.result.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Poison02
 * @date 2024/1/20
 */
@FeignClient(contextId = "user", name = "user-service", configuration = FeignInterceptor.class)
public interface UserFeignClient {

    @GetMapping("/api/captcha/image")
    Response<CaptchaVO> getCaptcha();

    @GetMapping("/api/getUserById")
    Response<UserSimpleVO> getUserById(@RequestParam("id") String userId);

}
