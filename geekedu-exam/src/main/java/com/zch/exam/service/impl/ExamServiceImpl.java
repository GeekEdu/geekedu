package com.zch.exam.service.impl;

import com.zch.api.vo.exam.ExamCountVO;
import com.zch.api.vo.exam.practice.PracticeFrontVO;
import com.zch.exam.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/3/4
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ExamServiceImpl implements IExamService {

    private final IQuestionsService questionsService;

    private final IPapersService papersService;

    private final IPracticeService practiceService;

    private final IMockService mockService;

    private final IChapterService chapterService;

    @Override
    public ExamCountVO getExamCount() {
        ExamCountVO vo = new ExamCountVO();
        // 查找练习和练习章节数
        long practiceCount = practiceService.practiceCount();
        long practiceChapterCount = chapterService.chapterCount();
        vo.setPracticeCount(practiceCount);
        vo.setPracticeChapterCount(practiceChapterCount);
        return vo;
    }

    @Override
    public PracticeFrontVO getPracticeList(Integer pageNum, Integer pageSize, Integer categoryId, Integer childId) {
        return practiceService.getPracticeList(pageNum, pageSize, categoryId, childId);
    }
}
