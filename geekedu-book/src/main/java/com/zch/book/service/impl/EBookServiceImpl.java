package com.zch.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.book.AddCommentForm;
import com.zch.api.dto.book.EBookForm;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.vo.book.*;
import com.zch.api.vo.book.comment.BCommentFullVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.book.domain.po.EBook;
import com.zch.book.mapper.EBookMapper;
import com.zch.book.service.IBCommentService;
import com.zch.book.service.IEBookArticleService;
import com.zch.book.service.IEBookChapterService;
import com.zch.book.service.IEBookService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.Response;
import com.zch.common.satoken.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/23
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EBookServiceImpl extends ServiceImpl<EBookMapper, EBook> implements IEBookService {

    private final EBookMapper bookMapper;

    private final LabelFeignClient labelFeignClient;

    private final IBCommentService commentService;

    private final IEBookChapterService chapterService;

    private final IEBookArticleService articleService;

    @Override
    public EBookAndCategoryVO getEBookPageByCondition(Integer pageNum, Integer pageSize, String sort, String order,
                                                      String keywords,
                                                      Integer categoryId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
        || StringUtils.isBlank(keywords) || ObjectUtils.isNull(categoryId)) {
            pageNum = 1;
            pageSize = 10;
            sort = "id";
            order = "desc";
        }
        EBookAndCategoryVO vo = new EBookAndCategoryVO();
        long count = count();
        if (count == 0) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.setCategories(new ArrayList<>(0));
        }
        LambdaQueryWrapper<EBook> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(EBook::getName, keywords);
        }
        if (ObjectUtils.isNotNull(categoryId)) {
            wrapper.eq(EBook::getCategoryId, categoryId);
        }
        // 增加排序
        wrapper.orderBy(true, "asc".equals(order), EBook::getId);
        Page<EBook> page = page(new Page<EBook>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page)) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.setCategories(new ArrayList<>(0));
        }
        List<EBook> records = page.getRecords();
        if (ObjectUtils.isNull(records) || CollUtils.isEmpty(records)) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.setCategories(new ArrayList<>(0));
        }
        List<EBookVO> list = new ArrayList<>(records.size());
        for (EBook book : records) {
            EBookVO vo1 = new EBookVO();
            BeanUtils.copyProperties(book, vo1);
            Response<CategorySimpleVO> res = labelFeignClient.getCategoryById(book.getCategoryId(), "E_BOOK");
            if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData())) {
                vo1.setCategory(null);
            }
            vo1.setCategory(res.getData());
            list.add(vo1);
        }
        vo.getData().setTotal(count);
        vo.getData().setData(list);
        Response<List<CategorySimpleVO>> response = labelFeignClient.getCategorySimpleList("E_BOOK");
        if (ObjectUtils.isNull(response) || ObjectUtils.isNull(response.getData())) {
            vo.setCategories(null);
        }
        vo.setCategories(response.getData());
        return vo;
    }

    @Override
    public List<EBookChapterVO> getChapterList(Integer bookId) {
        if (ObjectUtils.isNull(bookId)) {
            return new ArrayList<>(0);
        }
        return chapterService.getChapterListByBookId(bookId);
    }

    @Override
    public EBookArticleFullVO getArticlePage(Integer pageNum, Integer pageSize, String sort, String order,
                                             Integer bookId,
                                             Integer chapterId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
                || StringUtils.isBlank(sort) || StringUtils.isBlank(order)) {
            pageNum = 1;
            pageSize = 10;
            sort = "groundingTime";
            order = "desc";
        }
        return articleService.getArticlePageCondition(pageNum, pageSize, sort, order, bookId, chapterId);
    }

    @Override
    public EBookVO insertEBook(EBookForm form) {
        if (ObjectUtils.isNull(form)) {
            return new EBookVO();
        }
        // 用户id
        Long userId = UserContext.getLoginId();
        // 查询是否有该数据
        if (StringUtils.isBlank(form.getName())) {
            return new EBookVO();
        }
        EBook one = bookMapper.selectOne(new LambdaQueryWrapper<EBook>()
                .eq(EBook::getName, form.getName()));
        if (ObjectUtils.isNotNull(one)) {
            return new EBookVO();
        }
        EBook eBook = new EBook();
        eBook.setName(form.getName());
        eBook.setCoverLink(form.getCoverLink());
        eBook.setCategoryId(form.getCategoryId());
        eBook.setShortDesc(form.getShortDesc());
        eBook.setFullDesc(form.getFullDesc());
        eBook.setSellType(form.getSellType());
        eBook.setGroundingTime(form.getGroundingTime());
        eBook.setIsShow(form.getIsShow());
        eBook.setPrice(new BigDecimal(form.getPrice()));
        eBook.setIsVipFree(form.getIsVipFree());
        eBook.setCreatedBy(userId);
        eBook.setUpdatedBy(userId);
        save(eBook);
        EBookVO vo = new EBookVO();
        EBook res = bookMapper.selectOne(new LambdaQueryWrapper<EBook>()
                .eq(EBook::getName, form.getName()));
        BeanUtils.copyProperties(res, vo);
        return vo;
    }

    @Override
    public Boolean deleteEBook(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return false;
        }
        return removeById(id);
    }

    @Override
    public Boolean updateEBook(Integer id, EBookForm form) {
        if (ObjectUtils.isNull(id) || ObjectUtils.isNull(form)) {
            return false;
        }
        // 用户id
        Long userId = UserContext.getLoginId();
        EBook one = bookMapper.selectById(id);
        if (ObjectUtils.isNotNull(one)) {
            return false;
        }
        one.setName(form.getName());
        one.setCoverLink(form.getCoverLink());
        one.setShortDesc(form.getShortDesc());
        one.setFullDesc(form.getFullDesc());
        one.setIsShow(form.getIsShow());
        one.setSellType(form.getSellType());
        one.setPrice(new BigDecimal(form.getPrice()));
        one.setIsVipFree(form.getIsVipFree());
        one.setGroundingTime(form.getGroundingTime());
        one.setCategoryId(form.getCategoryId());
        one.setUpdatedBy(userId);
        return updateById(one);
    }

    @Override
    public EBookVO getEBookById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new EBookVO();
        }
        EBookVO vo = new EBookVO();
        EBook eBook = getById(id);
        if (ObjectUtils.isNull(eBook)) {
            return new EBookVO();
        }
        BeanUtils.copyProperties(eBook, vo);
        return vo;
    }

    @Override
    public EBookArticleVO getEBookArticleById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new EBookArticleVO();
        }
        return articleService.getEBookArticleById(id);
    }

    @Override
    public EBookAndCategoryVO getBookList(Integer pageNum, Integer pageSize, String scene, Integer categoryId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
        || StringUtils.isBlank(scene) || ObjectUtils.isNull(categoryId)) {
            pageNum = 1;
            pageSize = 10;
            scene = "default";
            categoryId = 0;
        }
        EBookAndCategoryVO vo = new EBookAndCategoryVO();
        // 查询分类
        Response<List<CategorySimpleVO>> res = labelFeignClient.getCategorySimpleList("E_BOOK");
        if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData()) || CollUtils.isEmpty(res.getData())) {
            vo.setCategories(new ArrayList<>(0));
        }
        vo.setCategories(res.getData());
        long count = count();
        if (count == 0) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<EBook> wrapper = new LambdaQueryWrapper<>();
        if (! Objects.equals(categoryId, 0)) {
            wrapper.eq(EBook::getCategoryId, categoryId);
        }
        if (StringUtils.isNotBlank(scene)) {
            if ("default".equals(scene)) {
                wrapper.orderBy(true, false, EBook::getId);
            }
        }
        Page<EBook> page = page(new Page<EBook>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        List<EBookVO> vos = page.getRecords().stream().map(item -> {
            EBookVO vo1 = new EBookVO();
            BeanUtils.copyProperties(item, vo1);
            Response<CategorySimpleVO> res2 = labelFeignClient.getCategoryById(item.getCategoryId(), "E_BOOK");
            if (ObjectUtils.isNull(res2) || ObjectUtils.isNull(res2.getData())) {
                vo1.setCategory(null);
            }
            vo1.setCategory(res2.getData());
            return vo1;
        }).collect(Collectors.toList());
        vo.getData().setTotal(count);
        vo.getData().setData(vos);
        return vo;
    }

    @Override
    public EBookFullVO getBookDetailById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new EBookFullVO();
        }
        EBook eBook = bookMapper.selectById(id);
        if (ObjectUtils.isNull(eBook)) {
            return new EBookFullVO();
        }
        EBookFullVO vo = new EBookFullVO();
        EBookVO vo1 = new EBookVO();
        BeanUtils.copyProperties(eBook, vo1);
        Response<CategorySimpleVO> res = labelFeignClient.getCategoryById(eBook.getCategoryId(), "E_BOOK");
        if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData())) {
            vo1.setCategory(null);
        }
        vo1.setCategory(res.getData());
        // 查找章节
        boolean hasChapter = true;
        List<EBookChapterVO> chapters = chapterService.getChapterListByBookId(eBook.getId());
        if (ObjectUtils.isNull(chapters) || CollUtils.isEmpty(chapters)) {
            hasChapter = false;
            vo.setChapters(new ArrayList<>(0));
        }
        vo.setChapters(chapters);
        // 查找文章 这里分为三种情况
        // 1. 没有章节时，使用 vo 里面的articles
        // 2. 有章节时，使用 vo 里面的articleMap
        // 3. 有章节但没有使用时，使用 vo 里面的articleMap
        List<EBookArticleVO> articles = new ArrayList<>();
        Map<Integer, List<EBookArticleVO>> articleMap = new HashMap<>();
        if (hasChapter) {
            // 先将有章节的文章查出来
            for (EBookChapterVO item : chapters) {
                List<EBookArticleVO> list = articleService.getArticleList(eBook.getId(), item.getId());
                if (CollUtils.isNotEmpty(list)) {
                    articleMap.put(item.getId(), list);
                }
            }
            // 再查询有章节但是没有使用章节的文章
            List<EBookArticleVO> list2 = articleService.getArticleList(eBook.getId(), null);
            if (CollUtils.isNotEmpty(list2)) {
                articleMap.put(0, list2);
            }
        } else {
            // 没有章节 直接查没有章节的文章，放入 articles
            List<EBookArticleVO> list3 = articleService.getArticleList(eBook.getId(), null);
            articles = list3.stream().filter(Objects::nonNull).collect(Collectors.toList());
        }
        vo.setArticles(articles);
        vo.setArticleMap(articleMap);
        vo.setBook(vo1);
        return vo;
    }

    @Override
    public BCommentFullVO getBookComments(Integer id, Integer pageNum, Integer pageSize) {
        EBook eBook = bookMapper.selectById(id);
        if (ObjectUtils.isNull(eBook)) {
            return new BCommentFullVO();
        }
        return commentService.getFullComment(id, pageNum, pageSize, "E_BOOK");
    }

    @Override
    public Integer addBookComment(Integer bookId, AddCommentForm form) {
        // 查询电子书是否存在
        EBook eBook = bookMapper.selectById(bookId);
        if (ObjectUtils.isNull(eBook)) {
            return 0;
        }
        return commentService.addComment(bookId, form, "E_BOOK");
    }
}
