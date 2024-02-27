package com.zch.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.exam.TagForm;
import com.zch.api.vo.exam.CTagsVO;
import com.zch.api.vo.exam.PaperAndCategoryVO;
import com.zch.api.vo.exam.PapersVO;
import com.zch.api.vo.exam.TagsVO;
import com.zch.exam.domain.po.Papers;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/27
 */
public interface IPapersService extends IService<Papers> {

    /**
     * 条件分页查询试卷列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @return
     */
    PaperAndCategoryVO getPapersPage(Integer pageNum, Integer pageSize, String sort, String order, String keywords, Integer categoryId);

    /**
     * 根据id返回试卷明细
     * @param id
     * @return
     */
    PapersVO getPaperById(Integer id);

    /**
     * 根据id删除试卷
     * @param id
     * @return
     */
    Boolean deletePaperById(Integer id);

    /**
     * 返回分类列表
     * @return
     */
    List<CTagsVO> getCategoryList();

    /**
     * 分页查找分类列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<TagsVO> getTagList(Integer pageNum, Integer pageSize);

    /**
     * 根据id查看分类明细
     * @param id
     * @return
     */
    CTagsVO getTagById(Integer id);

    /**
     * 新增分类
     * @param form
     * @return
     */
    Boolean addTag(TagForm form);

    /**
     * 更新分类
     * @param id
     * @param form
     * @return
     */
    Boolean updateTag(Integer id, TagForm form);

    /**
     * 删除分类
     * @param id
     * @return
     */
    Boolean deleteTag(Integer id);

}
