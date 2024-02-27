package com.zch.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.exam.TagForm;
import com.zch.api.vo.exam.CTagsVO;
import com.zch.api.vo.exam.TagsVO;
import com.zch.exam.domain.po.Tags;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/25
 */
public interface ITagsService extends IService<Tags> {

    /**
     * 返回分类列表
     * @param type
     * @return
     */
    List<TagsVO> getTagsList(String type);

    /**
     * 根据分类id查找分类
     * @param id
     * @param type
     * @return
     */
    TagsVO getTagByCondition(Integer id, String type);

    /**
     * 根据分类名查找分类
     * @param name
     * @param type
     * @return
     */
    TagsVO getTagByName(String name, String type);

    /**
     * 查找简单分类
     * @param id
     * @param type
     * @return
     */
    CTagsVO getSimpleTagById(Integer id, String type);

    /**
     * 查找简单分类列表
     * @param type
     * @return
     */
    List<CTagsVO> getSTagsList(String type);

    /**
     * 条件分页查找分类列表
     * @param pageNum
     * @param pageSize
     * @param type
     * @return
     */
    Page<TagsVO> getCategoryList(Integer pageNum, Integer pageSize, String type);

    /**
     * 删除分类
     * @param id
     * @return
     */
    Boolean deleteTag(Integer id, String type);

    /**
     * 更新分类
     * @param id
     * @param form
     * @return
     */
    Boolean updateTag(Integer id, TagForm form);

    /**
     * 新增分类
     * @param form
     * @return
     */
    Boolean addTag(TagForm form);

}
