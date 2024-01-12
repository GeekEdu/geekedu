package com.zch.label.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.label.TagForm;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.domain.vo.TagPageVO;


/**
 * @author Poison02
 * @date 2024/1/7
 */
public interface ITagService extends IService<Tag> {

    /**
     * 查询所有标签
     * @return
     */
    Page<TagPageVO> getTagList(CategoryTagQuery query);

    /**
     * 条件查询标签
     * @param query
     * @return
     */
    Page<TagPageVO> getTagByCondition(CategoryTagQuery query);

    /**
     * 新增标签
     * @param form
     * @return
     */
    boolean addTag(TagForm form);

    /**
     * 删除标签
     * @param id
     * @return
     */
    boolean deleteTag(Long id);

    /**
     * geng你标签
     * @param form
     * @return
     */
    Tag updateTag(TagForm form);

}
