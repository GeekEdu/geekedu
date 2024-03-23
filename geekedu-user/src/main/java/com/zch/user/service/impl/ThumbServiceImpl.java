package com.zch.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.user.ThumbForm;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.redis.constants.RedisConstants;
import com.zch.common.redis.utils.RedisUtils;
import com.zch.user.domain.po.Thumb;
import com.zch.user.enums.ThumbEnums;
import com.zch.user.mapper.ThumbMapper;
import com.zch.user.service.IThumbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.zch.user.enums.ThumbEnums.IMAGE_TEXT;

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
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        // 查找点赞表中是否已经有该记录
        /*
         * 查询redis中是否已经有该记录
         *   - 存在，则将该用户移除
         *   - 不存在，则加入一条记录
         *
         */
        String key = ThumbEnums.valueOf(form.getType()).equals(IMAGE_TEXT)
                ? RedisConstants.IMAGE_TEXT_SET + form.getRelationId()
                : RedisConstants.QA_COMMENT_SET + form.getRelationId();
        long cur = System.currentTimeMillis();
        boolean isExists = RedisUtils.rSetContainSingle(key, userId);
        if (isExists) {
            // 存在则 移除记录
            RedisUtils.removeRSetSingle(key, userId);
            // 返回取消点赞
            return false;
        } else {
            // 不存在 增加记录
            RedisUtils.addRSetSingle(key, cur, userId);
            // 返回点赞
            return true;
        }
        /**
         * 定时更新MySQL TODO
         */
//        LambdaQueryWrapper<Thumb> wrapper = new LambdaQueryWrapper<Thumb>()
//                .eq(Thumb::getRelationId, form.getId())
//                .eq(Thumb::getType, ThumbEnums.valueOf(form.getType()))
//                .eq(Thumb::getUserId, userId);
//        Thumb one = getOne(wrapper);
//        if (ObjectUtils.isNull(one)) {
//            // 如果没有该记录，则直接新插入一条
//            Thumb thumb = new Thumb();
//            thumb.setType(ThumbEnums.valueOf(form.getType()));
//            thumb.setUserId(userId);
//            thumb.setIsCancel(false);
//            thumb.setRelationId(form.getId());
//            save(thumb);
//            return true;
//        }
//        if (one.getIsCancel()) {
//            one.setIsCancel(false);
//            updateById(one);
//            return true;
//        } else {
//            one.setIsCancel(true);
//            updateById(one);
//            return false;
//        }
    }

    @Override
    public Boolean queryIsVote(Integer relationId, String type) {
        if (ObjectUtils.isNull(relationId) || StringUtils.isBlank(type)) {
            return false;
        }
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        String key = ThumbEnums.valueOf(type).equals(IMAGE_TEXT)
                ? RedisConstants.IMAGE_TEXT_SET + relationId
                : RedisConstants.QA_COMMENT_SET + relationId;
        boolean isExists = RedisUtils.rSetContainSingle(key, userId);
        if (isExists) {
            // 返回点赞
            return true;
        } else {
            // 返回未点赞
            return false;
        }
//        Thumb one = getOne(new LambdaQueryWrapper<Thumb>()
//                .eq(Thumb::getRelationId, relationId)
//                .eq(Thumb::getType, ThumbEnums.valueOf(type))
//                .eq(Thumb::getUserId, 1745747394693820416L));
//        if (ObjectUtils.isNull(one)) {
//            // 不存在则未点赞
//            return false;
//        }
//        // 如果存在，还需要看其是否取消
//        // 取消了 则还是未点赞
//        // 未取消 则点赞了
//        return !one.getIsCancel();
    }

    @Override
    public Long queryCount(Integer relationId, String type) {
        // Long userId = UserContext.getLoginId();
        if (ObjectUtils.isNull(relationId) || StringUtils.isBlank(type)) {
            return 0L;
        }
        Long userId = 1745747394693820416L;
        String key = ThumbEnums.valueOf(type).equals(IMAGE_TEXT)
                ? RedisConstants.IMAGE_TEXT_SET + relationId
                : RedisConstants.QA_COMMENT_SET + relationId;
        return (long) RedisUtils.rSetSize(key);
//        List<Thumb> thumbs = thumbMapper.selectList(new LambdaQueryWrapper<Thumb>()
//                .eq(Thumb::getRelationId, relationId)
//                .eq(Thumb::getType, ThumbEnums.valueOf(type))
//                .eq(Thumb::getIsCancel, false));
//        if (ObjectUtils.isNull(thumbs) || CollUtils.isEmpty(thumbs)) {
//            return 0L;
//        }
//        return (long) thumbs.size();
    }
}
