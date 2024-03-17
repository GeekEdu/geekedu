package com.zch.api.feignClient.user;

import com.zch.api.dto.user.CollectForm;
import com.zch.api.dto.user.ThumbForm;
import com.zch.api.interceptor.FeignInterceptor;
import com.zch.api.vo.user.CaptchaVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.result.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    Response<Boolean> thumbHandle(@RequestBody ThumbForm form);

    /**
     * 查询是否点过赞
     *
     * @param relationId
     * @param type
     * @return
     */
    @GetMapping("/api/thumb/isVote/{id}")
    Response<Boolean> queryIsVote(@PathVariable("id") Integer relationId, @RequestParam("type") String type);

    /**
     * 查询点赞数量
     *
     * @param relationId
     * @param type
     * @return
     */
    @GetMapping("/api/thumb/count/{id}")
    Response<Long> queryCount(@PathVariable("id") Integer relationId, @RequestParam("type") String type);

    /**
     * 后台 获取教师列表
     * @return
     */
    @GetMapping("/api/member/teacher/list")
    Response<List<UserSimpleVO>> getTeacherList();

    /**
     * 查询是否收藏
     * @param bookId
     * @param type
     * @return
     */
    @GetMapping("/api/collection/status")
    Response<Boolean> checkCollectStatus(@RequestParam("id") Integer bookId, @RequestParam("type") String type);

    /**
     * 点击 收藏图标
     * @param form
     * @return
     */
    @PostMapping("/api/collection/hit")
    Response<Boolean> hitCollectIcon(@RequestBody CollectForm form);

    /**
     * 查询收藏数量
     * @param relationId
     * @param type
     * @return
     */
    @GetMapping("/api/collection/count")
    Response<Long> collectionCount(@RequestParam("id") Integer relationId, @RequestParam("type") String type);

    /**
     * 获取vip价格
     * @param id
     * @return
     */
    @GetMapping("/api/member/vip/price")
    Response<BigDecimal> getVipPriceById(@RequestParam("id") Integer id);

}
