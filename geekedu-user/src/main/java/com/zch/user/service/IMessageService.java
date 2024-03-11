package com.zch.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.user.MessageForm;
import com.zch.api.vo.user.MessageVO;
import com.zch.user.domain.po.Message;

/**
 * @author Poison02
 * @date 2024/3/11
 */
public interface IMessageService extends IService<Message> {

    /**
     * 分页返回 站内消息 列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<MessageVO> getMessagePage(Integer pageNum, Integer pageSize);

    /**
     * 已读消息
     * @param messageId
     * @return
     */
    Boolean readMessage(String messageId);

    /**
     * 批量已读消息
     * @return
     */
    Boolean readMessageBatch();

    /**
     * 返回某个用户未读消息数
     * @return
     */
    Long unReadMessageCount();

    //===========================================================

    /**
     * 发送消息
     * @param userId
     * @param form
     * @return
     */
    Boolean sendMessage(Long userId, MessageForm form);

    /**
     * 批量发送消息
     * @param form
     * @return
     */
    Boolean sendMessageBatch(MessageForm form);

}
