package com.zch.exam.service;

import com.zch.api.vo.exam.ExamCountVO;
import com.zch.api.vo.exam.mock.MockFrontVO;
import com.zch.api.vo.exam.paper.PaperFrontVO;
import com.zch.api.vo.exam.practice.PracticeDetailVO;
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

    /**
     * 获取练习明细
     * @param id
     * @return
     */
    PracticeDetailVO getPracticeDetailById(Integer id);

    /**
     * 在线考试列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param childId
     * @return
     */
    PaperFrontVO getPaperList(Integer pageNum, Integer pageSize, Integer categoryId, Integer childId);

    /**
     * 模拟考试列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param childId
     * @return
     */
    MockFrontVO getMockList(Integer pageNum, Integer pageSize, Integer categoryId, Integer childId);

}
