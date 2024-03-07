package com.zch.exam.service.impl;

import com.zch.api.vo.exam.ChapterVO;
import com.zch.api.vo.exam.ExamCountVO;
import com.zch.api.vo.exam.PracticeVO;
import com.zch.api.vo.exam.mock.MockFrontVO;
import com.zch.api.vo.exam.paper.PaperFrontVO;
import com.zch.api.vo.exam.practice.PracticeDetailVO;
import com.zch.api.vo.exam.practice.PracticeFrontVO;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.exam.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        // 查找在线考试数 即试卷数
        long paperCount = papersService.getPaperCount();
        // 查找模拟考试数
        long mockCount = mockService.getMockCount();
        vo.setPaperCount(paperCount);
        vo.setMockCount(mockCount);
        return vo;
    }

    @Override
    public PracticeFrontVO getPracticeList(Integer pageNum, Integer pageSize, Integer categoryId, Integer childId) {
        return practiceService.getPracticeList(pageNum, pageSize, categoryId, childId);
    }

    @Override
    public PracticeDetailVO getPracticeDetailById(Integer id) {
        PracticeDetailVO vo = new PracticeDetailVO();
        if (ObjectUtils.isNull(id)) {
            return vo;
        }
        // 查找练习
        PracticeVO practice = practiceService.getPracticeById(id);
        if (ObjectUtils.isNull(practice)) {
            return vo;
        }
        vo.setPractice(practice);
        // 是否能练习 TODO
        vo.setCanDo(true);
        // 查找练习的所有章节
        List<ChapterVO> chapterList = chapterService.getChapterList(practice.getId());
        if (ObjectUtils.isNull(chapterList) || CollUtils.isEmpty(chapterList)) {
            vo.setChapters(new ArrayList<>(0));
            return vo;
        }
        vo.setChapters(chapterList);
        return vo;
    }

    @Override
    public PaperFrontVO getPaperList(Integer pageNum, Integer pageSize, Integer categoryId, Integer childId) {
        return papersService.getPaperList(pageNum, pageSize, categoryId, childId);
    }

    @Override
    public MockFrontVO getMockList(Integer pageNum, Integer pageSize, Integer categoryId, Integer childId) {
        return mockService.getMockList(pageNum, pageSize, categoryId, childId);
    }
}
