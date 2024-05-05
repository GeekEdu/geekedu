package com.zch.book.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.book.EBookArticleForm;
import com.zch.api.vo.book.*;
import com.zch.book.domain.po.EBookArticle;
import com.zch.book.mapper.EBookArticleMapper;
import com.zch.book.service.IEBookArticleService;
import com.zch.book.service.IEBookChapterService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.satoken.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        wrapper.orderBy(true, "asc".equals(order), EBookArticle::getGroundingTime);
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

    @Override
    public EBookArticleVO getEBookArticleById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return null;
        }
        EBookArticle eBookArticle = getById(id);
        EBookArticleVO vo = new EBookArticleVO();
        BeanUtils.copyProperties(eBookArticle, vo);
        EBookChapterVO chapter = chapterService.getChapterById(eBookArticle.getChapterId());
        if (ObjectUtils.isNull(chapter)) {
            vo.setChapter(null);
        }
        vo.setChapter(chapter);
        return vo;
    }

    @Override
    public Boolean deleteArticleById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return false;
        }
        EBookArticle article = getById(id);
        if (ObjectUtils.isNull(article)) {
            return false;
        }
        return removeById(id);
    }

    @Override
    public Boolean addArticle(EBookArticleForm form) {
        if (ObjectUtils.isNull(form)) {
            return false;
        }
        EBookArticle one = articleMapper.selectOne(new LambdaQueryWrapper<EBookArticle>()
                .eq(EBookArticle::getTitle, form.getTitle())
                .eq(EBookArticle::getBookId, form.getBookId())
                .eq(EBookArticle::getChapterId, form.getChapterId()));
        if (ObjectUtils.isNotNull(one)) {
            return false;
        }
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        EBookArticle article = new EBookArticle();
        article.setTitle(form.getTitle());
        article.setBookId(form.getBookId());
        article.setChapterId(form.getChapterId() == null ? 0 : form.getChapterId());
        article.setEditor(form.getEditor());
        article.setIsShow(form.getIsShow());
        article.setIsFreeRead(form.getIsFreeRead());
        article.setOriginalContent(form.getOriginalContent());
        article.setRenderContent(form.getRenderContent());
        article.setGroundingTime(form.getGroundingTime());
        article.setCreatedBy(userId);
        article.setUpdatedBy(userId);
        return save(article);
    }

    @Override
    public EBookArticleVO updateArticle(Integer id, EBookArticleForm form) {
        if (ObjectUtils.isNull(id)) {
            return null;
        }
        EBookArticle one = getById(id);
        if (ObjectUtils.isNull(one)) {
            return null;
        }
        EBookArticle article = new EBookArticle();
        article.setId(one.getId());
        article.setTitle(form.getTitle());
        article.setChapterId(form.getChapterId());
        article.setIsShow(form.getIsShow());
        article.setIsFreeRead(form.getIsFreeRead());
        article.setOriginalContent(form.getOriginalContent());
        article.setRenderContent(form.getRenderContent());
        article.setGroundingTime(form.getGroundingTime());
        updateById(article);
        EBookArticleVO vo = new EBookArticleVO();
        BeanUtils.copyProperties(one, vo);
        return vo;
    }

    @Override
    public List<EBookArticleVO> getArticleList(Integer bookId, Integer chapterId) {
        if (ObjectUtils.isNull(bookId)) {
            return new ArrayList<>(0);
        }
        List<EBookArticle> list;
        LambdaQueryWrapper<EBookArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EBookArticle::getBookId, bookId);
        if (ObjectUtils.isNotNull(chapterId)) {
            wrapper.eq(EBookArticle::getChapterId, chapterId);
            list = articleMapper.selectList(wrapper);
            if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
                return new ArrayList<>(0);
            }
            return list.stream().map(item -> {
                EBookArticleVO vo1 = new EBookArticleVO();
                BeanUtils.copyProperties(item, vo1);
                return vo1;
            }).collect(Collectors.toList());
        } else {
            // 为空
            wrapper.eq(EBookArticle::getChapterId, 0);
            list = articleMapper.selectList(wrapper);
            if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
                return new ArrayList<>(0);
            }
            return list.stream().map(item -> {
                EBookArticleVO vo1 = new EBookArticleVO();
                BeanUtils.copyProperties(item, vo1);
                return vo1;
            }).collect(Collectors.toList());
        }
    }

    @Override
    public EBookArticleSimpleVO getArticleSimple(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new EBookArticleSimpleVO();
        }
        EBookArticle article = articleMapper.selectById(id);
        if (ObjectUtils.isNull(article)) {
            return new EBookArticleSimpleVO();
        }
        EBookArticleSimpleVO vo = new EBookArticleSimpleVO();
        BeanUtils.copyProperties(article, vo);
        return vo;
    }

    @Override
    public List<Integer> getArticleIdList(Integer bookId) {
        List<EBookArticle> list = articleMapper.selectList(new LambdaQueryWrapper<EBookArticle>()
                .eq(EBookArticle::getBookId, bookId)
                .select(EBookArticle::getId));
        if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        return list.stream().map(EBookArticle::getId).collect(Collectors.toList());
    }

}
