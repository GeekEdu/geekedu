package com.zch.label.service;

import com.zch.api.dto.label.TagForm;
import com.zch.common.domain.Response;
import com.zch.common.domain.query.PageQuery;
import com.zch.common.domain.vo.PageReqVO;
import com.zch.common.domain.vo.PageVO;
import com.zch.label.domain.dto.TagDTO;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/7
 */
public interface ITagService {

    /**
     * 查询所有标签
     * @return
     */
    Response<PageVO<TagDTO>> getTagList(PageReqVO req);

    /**
     * 条件查询标签
     * @param query
     * @return
     */
    Response<PageQuery> getTagByCondition(CategoryTagQuery query);

    /**
     * 新增标签
     * @param form
     * @return
     */
    Response<Tag> addTag(TagForm form);

    /**
     * 删除标签
     * @param id
     * @return
     */
    Response<Tag> deleteTag(Long id);

    /**
     * geng你标签
     * @param form
     * @return
     */
    Response<Tag> updateTag(TagForm form);

}
