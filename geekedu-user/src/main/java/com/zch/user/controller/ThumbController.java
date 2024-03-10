package com.zch.user.controller;

import com.zch.api.dto.user.ThumbForm;
import com.zch.common.mvc.result.Response;
import com.zch.user.service.IThumbService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/3/10
 */
@RestController
@RequestMapping("/api/thumb")
@RequiredArgsConstructor
public class ThumbController {

    private final IThumbService thumbService;

    /**
     * 点赞和取消点赞操作
     * @param form
     * @return
     */
    @PostMapping("/vote")
    public Response<Boolean> thumbHandle(@RequestBody ThumbForm form) {
        return Response.success(thumbService.thumb(form));
    }

    /**
     * 查询是否点过赞
     * @param relationId
     * @param type
     * @return
     */
    @GetMapping("/isVote/{id}")
    public Response<Boolean> queryIsVote(@PathVariable("id") Integer relationId, @RequestParam("type") String type) {
        return Response.success(thumbService.queryIsVote(relationId, type));
    }

    /**
     * 查询点赞数量
     * @param relationId
     * @param type
     * @return
     */
    @GetMapping("/count/{id}")
    public Response<Long> queryCount(@PathVariable("id") Integer relationId, @RequestParam("type") String type) {
        return Response.success(thumbService.queryCount(relationId, type));
    }

}
