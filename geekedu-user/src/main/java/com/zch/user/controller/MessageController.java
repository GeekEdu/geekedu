package com.zch.user.controller;

import com.zch.api.dto.user.MessageForm;
import com.zch.api.vo.user.MessageVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.user.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/3/11
 */
@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;

    /**
     * 分页返回登录用户的消息列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/v2/list")
    public PageResult<MessageVO> getMessageList(@RequestParam("pageNum") Integer pageNum,
                                                @RequestParam("pageSize") Integer pageSize) {
        return PageResult.success(messageService.getMessagePage(pageNum, pageSize));
    }

    /**
     * 未读消息数
     * @return
     */
    @GetMapping("/v2/unReadMessage")
    public Response<Long> UnReadMessageCount() {
        return Response.success(messageService.unReadMessageCount());
    }

    /**
     * 已读消息
     * @param messageId
     * @return
     */
    @GetMapping("/v2/read/{id}")
    public Response<Boolean> readMessage(@PathVariable("id") String messageId) {
        return Response.success(messageService.readMessage(messageId));
    }

    /**
     * 全部已读
     * @return
     */
    @GetMapping("/v2/read/all")
    public Response<Boolean> readAllMessage() {
        return Response.success(messageService.readMessageBatch());
    }

    //===============================================================================================
    /**
     * 发送消息
     * @param userId
     * @param form
     * @return
     */
    @PostMapping("/send/{id}")
    public Response<Boolean> sendMessage(@PathVariable("id") Long userId,
                                         @RequestBody MessageForm form) {
        return Response.success(messageService.sendMessage(userId, form));
    }

    /**
     * 批量发送消息
     * @param form
     * @return
     */
    @PostMapping("/send/batch")
    public Response<Boolean> sendMessageBatch(@RequestBody MessageForm form) {
        return Response.success(messageService.sendMessageBatch(form));
    }

}
