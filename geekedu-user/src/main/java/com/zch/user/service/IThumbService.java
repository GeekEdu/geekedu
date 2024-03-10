package com.zch.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.user.ThumbForm;
import com.zch.user.domain.po.Thumb;

/**
 * @author Poison02
 * @date 2024/3/9
 */
public interface IThumbService extends IService<Thumb> {

    /**
     * 点赞 || 取消点赞
     * @return
     */
    Boolean thumb(ThumbForm form);

    /**
     * 查询是否点赞
     * @param relationId
     * @param type
     * @return
     */
    Boolean queryIsVote(Integer relationId, String type);

    /**
     * 查询点赞数量
     * @param relationId
     * @param type
     * @return
     */
    Long queryCount(Integer relationId, String type);

}
