package com.zch.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.user.CollectForm;
import com.zch.user.domain.po.Collection;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/16
 */
public interface ICollectionService extends IService<Collection> {

    /**
     * 检查收藏状态
     * @param relationId
     * @param type
     * @return
     */
    Boolean checkCollectionStatus(Integer relationId, String type);

    /**
     * 点击 收藏图标 收藏或者取消收藏
     * @param form
     * @return
     */
    Boolean hitCollectionIcon(CollectForm form);

    /**
     * 查询收藏数量
     * @param relationId
     * @param type
     * @return
     */
    Long queryCount(Integer relationId, String type);

    /**
     * 查询收藏列表
     * @param userId
     * @param type
     * @return
     */
    List<Collection> queryList(Long userId, String type);

}
