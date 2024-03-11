package com.zch.user.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.user.MessageForm;
import com.zch.api.vo.user.MessageVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.user.domain.po.Message;
import com.zch.user.mapper.MessageMapper;
import com.zch.user.service.IMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/3/11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    private final MessageMapper messageMapper;

    @Override
    public Page<MessageVO> getMessagePage(Integer pageNum, Integer pageSize) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)) {
            pageNum = 1;
            pageSize = 10;
        }
        Page<MessageVO> vo = new Page<>();
        long count = count();
        if (count == 0) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        Page<Message> page = page(new Page<Message>(pageNum, pageSize),
                new LambdaQueryWrapper<Message>()
                        .orderByDesc(Message::getCreatedTime));
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        vo.setRecords(page.getRecords().stream().map(item -> {
            MessageVO vo1 = new MessageVO();
            BeanUtils.copyProperties(item, vo1);
            return vo1;
        }).collect(Collectors.toList()));
        vo.setTotal(page.getTotal());
        return vo;
    }

    @Override
    public Boolean readMessage(String messageId) {
        if (StringUtils.isBlank(messageId)) {
            return false;
        }
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        return read(messageId, userId);
    }

    @Override
    public Boolean readMessageBatch() {
        // 查找当前登录用户所有未读的消息
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        List<Message> list = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .eq(Message::getIsRead, false)
                .eq(Message::getUserId, userId));
        if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
            return true;
        }
        for (Message item : list) {
            read(item.getId(), userId);
        }
        return true;
    }

    @Override
    public Long unReadMessageCount() {
        return messageMapper.selectCount(new LambdaQueryWrapper<Message>()
                .eq(Message::getIsRead, false));
    }

    @Override
    public Boolean sendMessage(Long userId, MessageForm form) {
        if (ObjectUtils.isNull(userId) || ObjectUtils.isNull(form) || ObjectUtils.isNull(form.getMessage())
        || StringUtils.isBlank(form.getMessage())) {
            return false;
        }
        return addMessage(form.getMessage(), userId);
    }

    @Override
    public Boolean sendMessageBatch(MessageForm form) {
        if (ObjectUtils.isNull(form) || ObjectUtils.isNull(form.getMessage()) || ObjectUtils.isNull(form.getUserIds())
         || CollUtils.isEmpty(form.getUserIds()) || StringUtils.isBlank(form.getMessage())) {
            return false;
        }
        String message = form.getMessage();
        for (Long id : form.getUserIds()) {
            addMessage(message, id);
        }
        return true;
    }

    /**
     * 公共读消息方法
     * @param messageId
     * @param userId
     * @return
     */
    private Boolean read(String messageId, Long userId) {
        Message message = getOne(new LambdaQueryWrapper<Message>()
                .eq(Message::getId, messageId)
                .eq(Message::getUserId, userId));
        if (ObjectUtils.isNull(message)) {
            return false;
        }
        if (message.getIsRead()) {
            return true;
        } else {
            message.setIsRead(true);
            message.setReadTime(LocalDateTime.now());
            updateById(message);
        }
        return true;
    }

    /**
     * 公共添加消息
     * @param content
     * @param userId
     * @return
     */
    private Boolean addMessage(String content, Long userId) {
        Message message = new Message();
        message.setId(UUID.fastUUID().toString());
        message.setMessage(content);
        message.setUserId(userId);
        message.setCreatedTime(LocalDateTime.now());
        return save(message);
    }
}
