package com.zch.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.user.CollectForm;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.redis.utils.RedisUtils;
import com.zch.user.domain.po.Collection;
import com.zch.user.enums.CollectionEnums;
import com.zch.user.mapper.CollectionMapper;
import com.zch.user.service.ICollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.zch.common.redis.constants.RedisConstants.*;

/**
 * @author Poison02
 * @date 2024/3/16
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements ICollectionService {

    private final CollectionMapper collectionMapper;

    @Override
    public Boolean checkCollectionStatus(Integer relationId, String type) {
        if (ObjectUtils.isNull(relationId) || StringUtils.isBlank(type)) {
            return false;
        }
        if (ObjectUtils.isNull(CollectionEnums.valueOf(type))) {
            return false;
        }
        // 用户id
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        String key = switch (CollectionEnums.valueOf(type)) {
            case E_BOOK -> E_BOOK_SET + relationId;
            case IMAGE_TEXT -> IMAGE_TEXT_SET + relationId;
            case REPLAY_COURSE -> REPLAY_COURSE_SET + relationId;
            case LIVE_COURSE -> LIVE_COURSE_SET + relationId;
        };
        boolean isExists = RedisUtils.rSetContainSingle(key, userId);
        if (isExists) {
            return true;
        } else {
            return false;
        }
//        Collection one = getOne(new LambdaQueryWrapper<Collection>()
//                .eq(Collection::getUserId, userId)
//                .eq(Collection::getRelationId, relationId)
//                .eq(Collection::getType, CollectionEnums.valueOf(type)));
//        // 没查到的是未收藏
//        if (ObjectUtils.isNull(one)) {
//            return false;
//        }
//        return !one.getIsCancel();
    }

    @Override
    public Boolean hitCollectionIcon(CollectForm form) {
        if (ObjectUtils.isNull(form) || ObjectUtils.isNull(form.getRelationId()) || StringUtils.isBlank(form.getType())) {
            return false;
        }
        // 用户id
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        String key = switch (CollectionEnums.valueOf(form.getType())) {
            case E_BOOK -> E_BOOK_SET + form.getRelationId();
            case IMAGE_TEXT -> IMAGE_TEXT_SET + form.getRelationId();
            case REPLAY_COURSE -> REPLAY_COURSE_SET + form.getRelationId();
            case LIVE_COURSE -> LIVE_COURSE_SET + form.getRelationId();
        };
        long cur = System.currentTimeMillis();
        boolean isExists = RedisUtils.rSetContainSingle(key, userId);
        if (isExists) {
            RedisUtils.removeRSetSingle(key, userId);
            return false;
        } else {
            RedisUtils.addRSetSingle(key, cur, userId);
            return true;
        }
//        Collection one = getOne(new LambdaQueryWrapper<Collection>()
//                .eq(Collection::getRelationId, form.getId())
//                .eq(Collection::getUserId, userId)
//                .eq(Collection::getType, CollectionEnums.valueOf(form.getType())));
//        if (ObjectUtils.isNull(one)) {
//            Collection collection = new Collection();
//            collection.setRelationId(form.getId());
//            collection.setUserId(userId);
//            collection.setType(CollectionEnums.valueOf(form.getType()));
//            save(collection);
//            return true;
//        }
//        // 如果存在 且是已取消的 则更新为点赞
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
    public Long queryCount(Integer relationId, String type) {
        if (ObjectUtils.isNull(relationId) || StringUtils.isBlank(type)) {
            return 0L;
        }
//        List<Collection> collections = list(new LambdaQueryWrapper<Collection>()
//                .eq(Collection::getRelationId, relationId)
//                .eq(Collection::getType, CollectionEnums.valueOf(type))
//                .eq(Collection::getIsCancel, false));
//        if (ObjectUtils.isNull(collections) || CollUtils.isEmpty(collections)) {
//            return 0L;
//        }
        Long userId = 1745747394693820416L;
        String key = switch (CollectionEnums.valueOf(type)) {
            case E_BOOK -> E_BOOK_SET + relationId;
            case IMAGE_TEXT -> IMAGE_TEXT_SET + relationId;
            case REPLAY_COURSE -> REPLAY_COURSE_SET + relationId;
            case LIVE_COURSE -> LIVE_COURSE_SET + relationId;
        };
        return (long) RedisUtils.rSetSize(key);
    }

    @Override
    public List<Collection> queryList(Long userId, String type) {
        List<Collection> list = list(new LambdaQueryWrapper<Collection>()
                .eq(Collection::getUserId, userId)
                .eq(Collection::getType, CollectionEnums.valueOf(type))
                .eq(Collection::getIsCancel, false));
        return CollUtils.isEmpty(list) ? new ArrayList<>(0) : list;
    }
}
