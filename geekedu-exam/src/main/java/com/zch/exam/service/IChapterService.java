package com.zch.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.exam.ChapterForm;
import com.zch.api.dto.exam.DelChapterForm;
import com.zch.api.vo.exam.ChapterVO;
import com.zch.exam.domain.po.Chapter;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/28
 */
public interface IChapterService extends IService<Chapter> {

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

    //============================================================
    // 前台

    /**
     * 返回章节 数量
     * @return
     */
    long chapterCount();

}
