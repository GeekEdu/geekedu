package com.zch.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.exam.ChapterForm;
import com.zch.api.dto.exam.DelChapterForm;
import com.zch.api.vo.exam.CTagsVO;
import com.zch.api.vo.exam.ChapterVO;
import com.zch.api.vo.exam.PracticeVO;
import com.zch.api.vo.exam.practice.PracticeFrontVO;
import com.zch.exam.domain.po.Practice;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/28
 */
public interface IPracticeService extends IService<Practice> {

    /**
     * 条件分页查找练习列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @return
     */
    Page<PracticeVO> getPracticePage(Integer pageNum, Integer pageSize, String sort, String order, String keywords, Integer categoryId);

    /**
     * 根据id查找练习明细
     * @param id
     * @return
     */
    PracticeVO getPracticeById(Integer id);

    /**
     * 返回分类列表
     * @return
     */
    List<CTagsVO> getTagList();

    /**
     * 获取练习的章节列表
     * @param practiceId
     * @return
     */
    List<ChapterVO> getChapterList(Integer practiceId);

    /**
     * 根据id获取章节
     * @param id
     * @return
     */
    ChapterVO getChapterById(Integer id);

    /**
     * 批量删除章节
     * @param form
     * @return
     */
    Boolean deleteChapterBatch(DelChapterForm form);

    /**
     * 新增章节
     * @param form
     * @return
     */
    Boolean addChapter(ChapterForm form);

    /**
     * 更新章节
     * @param id
     * @param form
     * @return
     */
    Boolean updateChapter(Integer id, ChapterForm form);

    //============================================================================
    // 前台

    /**
     * 返回练习 数量
     * @return
     */
    long practiceCount();

    /**
     * 前台 返回练习 数量
     * @param pageNum
     * @param pageSize
     * @param chapterId
     * @param childId
     * @return
     */
    PracticeFrontVO getPracticeList(Integer pageNum, Integer pageSize, Integer chapterId, Integer childId);

}
