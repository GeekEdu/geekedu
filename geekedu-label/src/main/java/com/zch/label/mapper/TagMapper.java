package com.zch.label.mapper;

import com.zch.common.domain.vo.PageReqVO;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.domain.vo.CategoryReqVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper {

    /**
     * 查找所有 tag
     * @return
     */
    List<Tag> selectTagList(PageReqVO req);

    /**
     * 查找某分类下的所有 tag
     * @param req
     * @return
     */
    List<Tag> selectTagAll(CategoryReqVO req);

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
