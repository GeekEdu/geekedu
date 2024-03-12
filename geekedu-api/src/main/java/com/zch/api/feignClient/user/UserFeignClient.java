package com.zch.api.feignClient.user;

import com.zch.api.dto.user.ThumbForm;
import com.zch.api.interceptor.FeignInterceptor;
import com.zch.api.vo.user.CaptchaVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.result.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/20
 */
@FeignClient(contextId = "user", name = "user-service", configuration = FeignInterceptor.class)
public interface UserFeignClient {

    /**
     * 验证码
     *
     * @return
     */
    @GetMapping("/api/captcha/image")
    Response<CaptchaVO> getCaptcha();

    /**
     * 根据id查询简单用户
     *
     * @param userId
     * @return
     */
    @GetMapping("/api/getUserById")
    Response<UserSimpleVO> getUserById(@RequestParam("id") String userId);

    /**
     * 点赞和取消点赞操作
     *
     * @param form
     * @return
     */
    @PostMapping("/api/thumb/vote")
    public Response<Boolean> thumbHandle(@RequestBody ThumbForm form);

    /**
     * 查询是否点过赞
     *
     * @param relationId
     * @param type
     * @return
     */
    @GetMapping("/api/thumb/isVote/{id}")
    public Response<Boolean> queryIsVote(@PathVariable("id") Integer relationId, @RequestParam("type") String type);

    /**
     * 查询点赞数量
     *
     * @param relationId
     * @param type
     * @return
     */
    @GetMapping("/api/thumb/count/{id}")
    public Response<Long> queryCount(@PathVariable("id") Integer relationId, @RequestParam("type") String type);

    /**
     * 后台 获取教师列表
     * @return
     */
    @GetMapping("/teacher/list")
    public Response<List<UserSimpleVO>> getTeacherList()

}
