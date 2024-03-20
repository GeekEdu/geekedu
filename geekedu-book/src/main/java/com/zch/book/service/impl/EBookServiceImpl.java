package com.zch.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.book.AddCommentForm;
import com.zch.api.dto.book.DelCommentBatchForm;
import com.zch.api.dto.book.EBookForm;
import com.zch.api.dto.label.CategoryForm;
import com.zch.api.dto.user.CollectForm;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.feignClient.trade.TradeFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.book.*;
import com.zch.api.vo.book.comment.BCommentFullVO;
import com.zch.api.vo.book.comment.BCommentVO;
import com.zch.api.vo.book.comment.CommentVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.label.CategoryVO;
import com.zch.book.domain.po.EBook;
import com.zch.book.domain.po.EBookArticle;
import com.zch.book.mapper.EBookMapper;
import com.zch.book.service.*;
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

    private final UserFeignClient userFeignClient;

    private final IBCommentService commentService;

    private final IEBookChapterService chapterService;

    private final IEBookArticleService articleService;

    private final ILearnRecordService learnRecordService;

    private final TradeFeignClient tradeFeignClient;

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
        vo.setCategory(labelFeignClient.getCategoryById(eBook.getCategoryId(), "E_BOOK").getData());
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
    public EBookSimpleVO getEBookSimple(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new EBookSimpleVO();
        }
        EBook eBook = bookMapper.selectById(id);
        if (ObjectUtils.isNull(eBook)) {
            return new EBookSimpleVO();
        }
        EBookSimpleVO vo = new EBookSimpleVO();
        BeanUtils.copyProperties(eBook, vo);
        return vo;
    }

    @Override
    public List<CategoryVO> getCategoryList() {
        Response<List<CategoryVO>> res = labelFeignClient.getCategoryList("E_BOOK");
        if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData()) || CollUtils.isEmpty(res.getData())) {
            return new ArrayList<>(0);
        }
        return res.getData();
    }

    @Override
    public CategorySimpleVO getCategoryDetail(Integer categoryId) {
        Response<CategorySimpleVO> res = labelFeignClient.getCategoryById(categoryId, "E_BOOK");
        if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData())) {
            return new CategorySimpleVO();
        }
        return res.getData();
    }

    @Override
    public Boolean deleteCategory(Integer categoryId) {
        return labelFeignClient.deleteCategoryById(categoryId).getData();
    }

    @Override
    public Boolean updateCategory(Integer categoryId, CategoryForm form) {
        return labelFeignClient.updateCategoryById(categoryId, form).getData();
    }

    @Override
    public Boolean addCategory(CategoryForm form) {
        return labelFeignClient.addCategory(form).getData();
    }

    @Override
    public Page<BCommentVO> getCommentList(Integer pageNum, Integer pageSize, String cType, List<String> createdTime) {
        Page<BCommentVO> page = commentService.getBackendComments(pageNum, pageSize, cType, createdTime);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            return new Page<>();
        }
        List<BCommentVO> list = page.getRecords().stream().map(item -> {
            // 判断类型，根据类型查找对应信息
            if (StringUtils.isNotBlank(cType)) {
                if ("E_BOOK".equals(cType)) {
                    EBookSimpleVO book = getEBookSimple(item.getRelationId());
                    if (ObjectUtils.isNotNull(book)) {
                        item.setEBook(book);
                    }
                } else if ("E_BOOK_ARTICLE".equals(cType)) {
                    EBookArticleSimpleVO article = articleService.getArticleSimple(item.getRelationId());
                    if (ObjectUtils.isNotNull(article)) {
                        item.setArticle(article);
                    }
                }
            }
            return item;
        }).collect(Collectors.toList());
        page.setRecords(list);
        return page;
    }

    @Override
    public Boolean deleteComment(Integer commentId, String cType) {
        return commentService.deleteCommentById(commentId, cType);
    }

    @Override
    public Boolean deleteCommentBatch(DelCommentBatchForm form) {
        return commentService.deleteCommentBatch(form);
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
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
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
        // 查找是否购买
        if (! BigDecimal.ZERO.equals(eBook.getPrice())) {
            Response<Boolean> res1 = tradeFeignClient.queryOrderIsPay(userId, id, "E_BOOK");
            if (ObjectUtils.isNotNull(res1) && ObjectUtils.isNotNull(res1.getData())) {
                vo.setIsBuy(res1.getData());
            }
        }
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
    public Boolean checkBookCollectionStatus(Integer bookId, String type) {
        if (ObjectUtils.isNull(bookId) || StringUtils.isBlank(type)) {
            return false;
        }
        return userFeignClient.checkCollectStatus(bookId, type).getData();
    }

    @Override
    public Boolean hitBookCollectionIcon(CollectForm form) {
        if (ObjectUtils.isNull(form) || ObjectUtils.isNull(form.getId()) || StringUtils.isBlank(form.getType())) {
            return false;
        }
        return userFeignClient.hitCollectIcon(form).getData();
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

    @Override
    public ArticleFullVO readArticle(Integer articleId) {
        // 构建返回对象
        ArticleFullVO vo = new ArticleFullVO();
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        // 根据id获取文章明细
        EBookArticleVO article = articleService.getEBookArticleById(articleId);
        if (ObjectUtils.isNull(article)) {
            return vo;
        }
        // 存在这篇文章，则将这篇文章对应的电子书id查出来，先构造电子书信息
        // 直接调用查询文章明细时的方法即可，都是相似的
        EBookFullVO bookVO = getBookDetailById(article.getBookId());
        vo.setArticles(bookVO.getArticles());
        vo.setArticleMap(bookVO.getArticleMap());
        vo.setBook(bookVO.getBook());
        vo.setChapters(bookVO.getChapters());
        vo.setArticle(article);
        // 上一节和下一节由后端返回
        // 这里有一个思路是：将该电子书的所有文章查出来，只需要文章id即可，
        // 放在一个列表里面，只要保证有序即可，每次请求传过来是文章id，那么只需要在这里列表里面查找对应的id
        // 然后返回此索引的前一个和后一个即可，如果遇到边界，那么按照实际来，设为0
        // 这里暂未考虑删除文章之后的 id 列表变化！
        List<Integer> ids = articleService.getArticleIdList(article.getBookId());
        if (ObjectUtils.isNull(ids) || CollUtils.isEmpty(ids)) {
            vo.setPrevId(0);
            vo.setNextId(0);
            return vo;
        }
        // 列表只有一个id且就是当前文章id时
        if (ids.contains(articleId) && ids.size() == 1) {
            vo.setPrevId(0);
            vo.setNextId(0);
            return vo;
        }
        // 返回当前文章id在列表中的位置
        int index = ids.indexOf(articleId);
        int head = 0; // 首
        int tail = ids.size() - 1; // 尾
        /*
        分为几种情况 前提都是ids的长度 > 1
            当前位置在 head ，则存在next
            当前位置在 tail ，则存在prev
            当前位置在 head 和 tail 之间，则 prev 和 next 都存在
         */
        if (index == head) {
            vo.setNextId(ids.get(index + 1));
            vo.setPrevId(0);
        } else if (index == tail) {
            vo.setPrevId(ids.get(index - 1));
            vo.setNextId(0);
        } else if ((index > head) && (index < tail)) {
            vo.setPrevId(ids.get(index - 1));
            vo.setNextId(ids.get(index + 1));
        }
        // 记录学习记录
        learnRecordService.updateLearnRecord(article.getBookId(), articleId, null, userId, "BOOK");
        return vo;
    }

    @Override
    public Page<CommentVO> getArticleComments(Integer articleId, Integer pageNum, Integer pageSize, Integer commentId) {
        if (ObjectUtils.isNull(articleId)) {
            return new Page<>();
        }
        // 查询是否有这篇文章
        EBookArticle article = articleService.getById(articleId);
        if (ObjectUtils.isNull(article)) {
            return new Page<>();
        }
        return commentService.getCommentPage(articleId, pageNum, pageSize, commentId, "E_BOOK_ARTICLE");
    }

    @Override
    public Integer addArticleComment(Integer articleId, AddCommentForm form) {
        if (ObjectUtils.isNull(articleId)) {
            return 0;
        }
        // 查询文章是否存在
        EBookArticle article = articleService.getById(articleId);
        if (ObjectUtils.isNull(article)) {
            return 0;
        }
        return commentService.addComment(articleId, form, "E_BOOK_ARTICLE");
    }

    public static void main(String[] args) {
        List<Integer> test = new LinkedList<>();
        test.add(1);
        test.add(2);
        test.add(3);
        test.add(4);
        test.add(5);
        int prev = 0;
        int next = 0;
        ListIterator<Integer> iterator = test.listIterator();
        while (iterator.hasNext()) {
            int current = iterator.next();
            if (current == 3) {
                // 获取当前元素的前一个节点
                if (iterator.hasPrevious()) {
                    prev = iterator.previous();
                    System.out.println("Previous Element: " + prev);
                    iterator.next(); // 移动回到当前位置
                }

                // 获取当前元素的后一个节点
                if (iterator.hasNext()) {
                    next = iterator.next();
                    System.out.println("Next Element: " + next);
                    iterator.previous(); // 移动回到当前位置
                }
            }
        }
        System.out.println(prev + " " + next);
    }
}
