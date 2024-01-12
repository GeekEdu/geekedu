package com.zch.label.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.domain.vo.CategoryReqVO;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 条件查询 tag
     * @param query
     * @return
     */
    List<Tag> selectTagByCondition(CategoryTagQuery query);

    /**
     * 根据 id 查找 tag
     * @param id
     * @return
     */
    Tag selectTagById(Long id);

    /**
     * 根据 标签名 查询标签
     * @param name
     * @return
     */
    Tag selectTagByName(String name);

    /**
     * 新增 tag
     * @param tag
     * @return
     */
    int insertTag(Tag tag);

    /**
     * 删除 tag
     * @param tag
     * @return
     */
    int deleteTag(Tag tag);

    /**
     * 批量删除标签
     * @param ids
     * @return
     */
    int deleteTagBatch(List<Long> ids);

    /**
     * 更新 tag
     * @param tag
     * @return
     */
    int updateTag(Tag tag);

}
