package com.zch.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.book.EBookArticleFullVO;
import com.zch.api.vo.book.EBookArticleVO;
import com.zch.api.vo.book.EBookChapterVO;
import com.zch.book.domain.po.EBookArticle;
import com.zch.book.mapper.EBookArticleMapper;
import com.zch.book.service.IEBookArticleService;
import com.zch.book.service.IEBookChapterService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/23
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EBookArticleServiceImpl extends ServiceImpl<EBookArticleMapper, EBookArticle> implements IEBookArticleService {

    private final EBookArticleMapper articleMapper;

    private final IEBookChapterService chapterService;

    @Override
    public EBookArticleFullVO getArticlePageCondition(Integer pageNum, Integer pageSize, String sort, String order,
                                                      Integer bookId,
                                                      Integer chapterId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
        || StringUtils.isBlank(sort) || StringUtils.isBlank(order)) {
            pageNum = 1;
            pageSize = 10;
            sort = "groundingTime";
            order = "desc";
        }
        EBookArticleFullVO vo = new EBookArticleFullVO();
        long count = count();
        if (count == 0) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.setChapters(new ArrayList<>(0));
        }
        LambdaQueryWrapper<EBookArticle> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotNull(bookId)) {
            wrapper.eq(EBookArticle::getBookId, bookId);
        }
        if (ObjectUtils.isNotNull(chapterId)) {
            wrapper.eq(EBookArticle::getChapterId, chapterId);
        }
        // 增加排序
        wrapper.orderBy(true, "asc".equals(order), EBookArticle::getCreatedTime);
        Page<EBookArticle> page = page(new Page<>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords())
                || CollUtils.isEmpty(page.getRecords())) {
            vo.getData().setTotal(count);
            vo.getData().setData(new ArrayList<>(0));
            vo.setChapters(new ArrayList<>(0));
        }
        List<EBookArticle> records = page.getRecords();
        List<EBookArticleVO> res = new ArrayList<>(records.size());
        for (EBookArticle article : records) {
            EBookArticleVO vo1 = new EBookArticleVO();
            BeanUtils.copyProperties(article, vo1);
            EBookChapterVO chapter = chapterService.getChapterById(article.getChapterId());
            if (ObjectUtils.isNull(chapter)) {
                vo1.setChapter(null);
            }
            vo1.setChapter(chapter);
            res.add(vo1);
        }
        List<EBookChapterVO> chapters = chapterService.getChapterListByBookId(bookId);
        if (ObjectUtils.isNull(chapters) || CollUtils.isEmpty(chapters)) {
            vo.setChapters(new ArrayList<>(0));
        }
        vo.getData().setData(res);
        vo.getData().setTotal(count);
        vo.setChapters(chapters);
        return vo;
    }
}
