package com.zch.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.ask.ImageTextCommentForm;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.book.comment.BCommentVO;
import com.zch.api.vo.book.comment.CommentVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.book.domain.po.BComment;
import com.zch.book.enums.CommentEnums;
import com.zch.book.mapper.BCommentMapper;
import com.zch.book.service.IBCommentService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.Response;
import com.zch.common.satoken.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Poison02
 * @date 2024/3/3
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BCommentServiceImpl extends ServiceImpl<BCommentMapper, BComment> implements IBCommentService {

    private final UserFeignClient userFeignClient;

    private final BCommentMapper commentMapper;

    @Override
    public Page<CommentVO> getCommentPage(Integer relationId, Integer pageNum, Integer pageSize, Integer commentId, String cType) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize) || ObjectUtils.isNull(commentId)) {
            pageNum = 1;
            pageSize = 10000;
            commentId = 0;
        }
        Page<CommentVO> vo = new Page<>();
        if (StringUtils.isBlank(cType)) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        long count = count();
        if (count == 0) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        LambdaQueryWrapper<BComment> wrapper = new LambdaQueryWrapper<>();
        if (Objects.equals(commentId, 0)) {
            // 等于 0 就是查所有一级评论 且按照创建时间降序
            wrapper.eq(BComment::getRelationId, relationId)
                    .eq(BComment::getCType, CommentEnums.valueOf(cType))
                    .eq(BComment::getParentId, 0)
                    .orderBy(true, false, BComment::getCreatedTime);
        } else {
            // 不等于0 就是查 此评论下的所有评论
            wrapper.eq(BComment::getRelationId, relationId)
                    .eq(BComment::getParentId, commentId)
                    .eq(BComment::getCType, CommentEnums.valueOf(cType));
        }
        // 查询一级评论
        Page<BComment> page = page(new Page<BComment>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        List<BComment> records = page.getRecords();
        List<CommentVO> list = new ArrayList<>(records.size());
        // 在这里要分两种情况
        // 1. commentId == 0 查询一级评论，就直接查出来即可
        // 2. commentId != 0 查询二级评论，还需要查二级评论的回复评论，还需要构造一个回复对象
        for (BComment item : records) {
            CommentVO vo1 = new CommentVO();
            BeanUtils.copyProperties(item, vo1);
            Response<UserSimpleVO> res1 = userFeignClient.getUserById(item.getUserId() + "");
            if (ObjectUtils.isNull(res1) || ObjectUtils.isNull(res1.getData())) {
                vo1.setUser(null);
            } else {
                vo1.setUser(res1.getData());
            }
            // 在这里判断是否有回复对象，通过replyId查找回复对象
            if (item.getReplyId() != 0) {
                // replyId就是另外一个评论的id
                BComment reply = commentMapper.selectById(item.getReplyId());
                BCommentVO vo2 = new BCommentVO();
                if (ObjectUtils.isNull(reply)) {
                    vo1.setReply(null);
                } else {
                    Response<UserSimpleVO> res2 = userFeignClient.getUserById(item.getUserId() + "");
                    if (ObjectUtils.isNull(res2) || ObjectUtils.isNull(res2.getData())) {
                        vo2.setUser(null);
                    } else {
                        vo2.setUser(res2.getData());
                    }
                    BeanUtils.copyProperties(reply, vo2);
                    vo1.setReply(vo2);
                }
            }
            list.add(vo1);
        }
        vo.setTotal(count);
        vo.setRecords(list);
        return vo;
    }

    @Override
    public CommentVO getCommentById(Integer commentId, String cType) {
        return null;
    }

    @Override
    public Integer addComment(Integer id, ImageTextCommentForm form, String cType) {
        if (ObjectUtils.isNull(form) || ObjectUtils.isNull(form.getContent()) || StringUtils.isBlank(form.getContent())) {
            return 0;
        }
        Long userId = UserContext.getLoginId();
        BComment comment = new BComment();
        comment.setContent(form.getContent());
        comment.setCType(CommentEnums.valueOf(cType));
        comment.setParentId(form.getParentId() == null ? 0 : form.getParentId());
        comment.setReplyId(form.getReplyId() == null ? 0 : form.getReplyId());
        comment.setRelationId(id);
//        comment.setUserId(userId);
        comment.setUserId(1745747394693820416L);
        commentMapper.insert(comment);
        // 如果是子评论，则父级评论的子评论数需要加1
        if (ObjectUtils.isNotNull(form.getParentId()) && form.getParentId() != 0) {
            BComment pOne = commentMapper.selectOne(new LambdaQueryWrapper<BComment>()
                    .eq(BComment::getRelationId, id)
                    .eq(BComment::getCType, CommentEnums.valueOf(cType))
                    .eq(BComment::getId, form.getParentId()));
            if (ObjectUtils.isNotNull(pOne)) {
                pOne.setChildrenCount(pOne.getChildrenCount() + 1);
                commentMapper.updateById(pOne);
            }
        }
        BComment one = commentMapper.selectOne(new LambdaQueryWrapper<BComment>()
                .eq(BComment::getRelationId, id)
                .eq(BComment::getContent, form.getContent())
                .eq(BComment::getCType, CommentEnums.valueOf(cType))
                .eq(BComment::getUserId, 1745747394693820416L)
                .last(" limit 1"));
        return one.getId();
    }
}
