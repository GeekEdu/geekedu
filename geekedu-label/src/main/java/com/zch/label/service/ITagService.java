package com.zch.label.service;

import com.zch.api.dto.label.TagForm;
import com.zch.common.domain.query.PageQuery;
import com.zch.common.domain.vo.PageReqVO;
import com.zch.common.domain.vo.PageVO;
import com.zch.label.domain.dto.TagDTO;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;


/**
 * @author Poison02
 * @date 2024/1/7
 */
public interface ITagService {

    /**
     * 查询所有标签
     * @return
     */
    PageVO<TagDTO> getTagList(PageReqVO req);

    /**
     * 条件查询标签
     * @param query
     * @return
     */
    PageVO<TagDTO> getTagByCondition(CategoryTagQuery query);

    /**
     * 新增标签
     * @param form
     * @return
     */
    Tag addTag(TagForm form);

    /**
     * 删除标签
     * @param id
     * @return
     */
    Tag deleteTag(Long id);

    /**
     * geng你标签
     * @param form
     * @return
     */
    Tag updateTag(TagForm form);

}
