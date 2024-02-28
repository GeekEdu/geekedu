package com.zch.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.user.TagVO;
import com.zch.user.domain.po.Tag;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/28
 */
public interface ITagService extends IService<Tag> {

    /**
     * 返回标签列表
     * @return
     */
    List<TagVO> getTagList();

    /**
     * 根据id获取标签
     * @param id
     * @return
     */
    TagVO getTagById(Integer id);

}
