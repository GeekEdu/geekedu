package com.zch.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.vo.course.live.LiveChapterVO;
import com.zch.course.domain.po.LiveChapter;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/12
 */
public interface ILiveChapterService extends IService<LiveChapter> {

    /**
     * 查找某个课程的章节列表
     * @param courseId
     * @return
     */
    List<LiveChapterVO> getChapterList(Integer courseId);

    /**
     * 根据章节id删除章节
     * @param id
     * @return
     */
    Boolean deleteChapterById(Integer id);

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

    /**
     * 获取章节明细
     * @param id
     * @return
     */
    LiveChapterVO getChapterById(Integer id);

}
