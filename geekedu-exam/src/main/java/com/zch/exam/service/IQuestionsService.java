package com.zch.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.exam.TagForm;
import com.zch.api.vo.exam.QuestionsFullVO;
import com.zch.api.vo.exam.QuestionsVO;
import com.zch.api.vo.exam.TagsVO;
import com.zch.exam.domain.po.Questions;

/**
 * @author Poison02
 * @date 2024/2/25
 */
public interface IQuestionsService extends IService<Questions> {

    /**
     * 根据id获取题目明细
     * @param id
     * @return
     */
    QuestionsVO getQuestionsById(Integer id);

    /**
     * 条件分页查找题库列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param type
     * @param level
     * @return
     */
    QuestionsFullVO getQuestionPage(Integer pageNum, Integer pageSize, Integer categoryId, Integer type, Integer level);

    /**
     * 条件分页查找分类列表
     * @param pageNum
     * @param pageSize
     * @param type
     * @return
     */
    Page<TagsVO> getCategoryList(Integer pageNum, Integer pageSize, String type);

    /**
     * 根据id删除分类
     * @param id
     * @param type
     * @return
     */
    Boolean deleteCategoryById(Integer id, String type);

    /**
     * 更新分类
     * @param id
     * @param form
     * @return
     */
    Boolean updateCategory(Integer id, TagForm form);

    /**
     * 新增分类
     * @param form
     * @return
     */
    Boolean addCategory(TagForm form);

    /**
     * 根据id查找分类明细
     * @param id
     * @return
     */
    TagsVO getCategoryById(Integer id, String type);

}
