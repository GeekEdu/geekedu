package com.zch.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.user.ThumbForm;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.user.domain.po.Thumb;
import com.zch.user.enums.ThumbEnums;
import com.zch.user.mapper.ThumbMapper;
import com.zch.user.service.IThumbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ThumbServiceImpl extends ServiceImpl<ThumbMapper, Thumb> implements IThumbService {

    private final ThumbMapper thumbMapper;

    @Override
    public Boolean thumb(ThumbForm form) {
        if (ObjectUtils.isNull(form) || ObjectUtils.isNull(form.getRelationId()) || StringUtils.isBlank(form.getType())) {
            return false;
        }
        if (ObjectUtils.isNull(ThumbEnums.valueOf(form.getType()))) {
            return false;
        }
        // 当前登录用户
        Long userId = UserContext.getLoginId();
        // 查找点赞表中是否已经有该记录
        LambdaQueryWrapper<Thumb> wrapper = new LambdaQueryWrapper<Thumb>()
                .eq(Thumb::getRelationId, form.getRelationId())
                .eq(Thumb::getType, ThumbEnums.valueOf(form.getType()));
        Thumb one = getOne(wrapper);
        if (ObjectUtils.isNull(one)) {
            // 如果没有该记录，则直接新插入一条
            Thumb thumb = new Thumb();
            thumb.setType(ThumbEnums.valueOf(form.getType()));
            thumb.setUserId(userId);
            thumb.setIsCancel(false);
            thumb.setRelationId(form.getRelationId());
            return save(thumb);
        } else {
            // 如果已经存在该记录，那么操作就是取消点赞
            one.setIsCancel(true);
            return updateById(one);
        }
    }

    @Override
    public Boolean queryIsVote(Integer relationId, String type) {
        if (ObjectUtils.isNull(relationId) || StringUtils.isBlank(type)) {
            return false;
        }
        Long userId = UserContext.getLoginId();
        Thumb one = getOne(new LambdaQueryWrapper<Thumb>()
                .eq(Thumb::getRelationId, relationId)
                .eq(Thumb::getType, ThumbEnums.valueOf(type))
                .eq(Thumb::getUserId, userId));
        if (ObjectUtils.isNull(one)) {
            // 不存在则未点赞
            return false;
        }
        // 如果存在，还需要看其是否取消
        if (one.getIsCancel()) {
            // 取消了 则还是未点赞
            return false;
        } else {
            // 未取消 则点赞了
            return true;
        }
    }

    @Override
    public Long queryCount(Integer relationId, String type) {
        Long userId = UserContext.getLoginId();
        if (ObjectUtils.isNull(relationId) || StringUtils.isBlank(type)) {
            return 0L;
        }
        List<Thumb> thumbs = thumbMapper.selectList(new LambdaQueryWrapper<Thumb>()
                .eq(Thumb::getRelationId, relationId)
                .eq(Thumb::getType, ThumbEnums.valueOf(type))
                .eq(Thumb::getUserId, userId)
                .eq(Thumb::getIsCancel, false));
        if (ObjectUtils.isNull(thumbs) || CollUtils.isEmpty(thumbs)) {
            return 0L;
        }
        return (long) thumbs.size();
    }
}
