package com.zch.exam.service;

import com.zch.api.vo.exam.ExamCountVO;
import com.zch.api.vo.exam.practice.PracticeFrontVO;

/**
 * @author Poison02
 * @date 2024/3/4
 */
public interface IExamService {

    /**
     * 前台 考试首页数量
     * @return
     */
    ExamCountVO getExamCount();

    /**
     * 前台 返回练习 列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param childId
     * @return
     */
    PracticeFrontVO getPracticeList(Integer pageNum, Integer pageSize, Integer categoryId, Integer childId);

}
