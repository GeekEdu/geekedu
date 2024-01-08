package com.zch.label.mapper;

import com.zch.common.annotation.Page;
import com.zch.common.domain.vo.PageReqVO;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;
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
     * 条件查询 tag
     * @param query
     * @return
     */
    @Page
    List<Tag> selectTagByCondition(CategoryTagQuery query);

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
     * 更新 tag
     * @param tag
     * @return
     */
    int updateTag(Tag tag);

}
