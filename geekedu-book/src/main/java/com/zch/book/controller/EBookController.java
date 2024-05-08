package com.zch.book.controller;

import com.zch.api.dto.book.*;
import com.zch.api.dto.label.CategoryForm;
import com.zch.api.dto.user.CollectForm;
import com.zch.api.vo.book.*;
import com.zch.api.vo.book.comment.BCommentFullVO;
import com.zch.api.vo.book.comment.BCommentVO;
import com.zch.api.vo.book.comment.CommentVO;
import com.zch.api.vo.book.record.StudyRecordVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.label.CategoryVO;
import com.zch.book.service.IEBookArticleService;
import com.zch.book.service.IEBookChapterService;
import com.zch.book.service.IEBookService;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/23
 */
@RestController
@RequestMapping("/api/eBook")
@RequiredArgsConstructor
public class EBookController {

    private final IEBookService eBookService;

    private final IEBookChapterService chapterService;

    private final IEBookArticleService articleService;

    /**
     * 条件分页查询电子书
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @return
     */
    @GetMapping("/getEBookPage")
    public Response<EBookAndCategoryVO> getEBookPageCondition(@RequestParam("pageNum") Integer pageNum,
                                                              @RequestParam("pageSize") Integer pageSize,
                                                              @RequestParam("sort") String sort,
                                                              @RequestParam("order") String order,
                                                              @RequestParam(value = "keywords", required = false) String keywords,
                                                              @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        return Response.success(eBookService.getEBookPageByCondition(pageNum, pageSize, sort, order, keywords, categoryId));
    }

    /**
     * 返回章节列表
     * @param bookId 文章id
     * @return
     */
    @GetMapping("/chapter/list")
    public Response<List<EBookChapterVO>> getChapterListById(@RequestParam("bookId") Integer bookId) {
        return Response.success(eBookService.getChapterList(bookId));
    }

    /**
     * 条件分页查询文章列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param bookId
     * @param chapterId
     * @return
     */
    @GetMapping("/article/list")
    public Response<EBookArticleFullVO> getArticleList(@RequestParam("pageNum") Integer pageNum,
                                                       @RequestParam("pageSize") Integer pageSize,
                                                       @RequestParam("sort") String sort,
                                                       @RequestParam("order") String order,
                                                       @RequestParam("bookId") Integer bookId,
                                                       @RequestParam(value = "chapterId", required = false) Integer chapterId) {
        return Response.success(eBookService.getArticlePage(pageNum, pageSize, sort, order, bookId, chapterId));
    }

    /**
     * 新增电子书
     * @param form
     * @return
     */
    @PostMapping("/add")
    public Response addEBook(@RequestBody EBookForm form) {
        return Response.success(eBookService.insertEBook(form));
    }

    /**
     * 根据id删除电子书
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public Response<Boolean> deleteEBook(@PathVariable("id") Integer id) {
        return Response.success(eBookService.deleteEBook(id));
    }

    /**
     * 更新电子书
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/update/{id}")
    public Response<Boolean> updateEBook(@PathVariable("id") Integer id, @RequestBody EBookForm form) {
        return Response.success(eBookService.updateEBook(id, form));
    }

    /**
     * 根据id获取电子书明细
     * @param id
     * @return
     */
    @GetMapping("/getEBookById/{id}")
    public Response<EBookVO> getEBookById(@PathVariable("id") Integer id) {
        return Response.success(eBookService.getEBookById(id));
    }

    /**
     * 获取电子书简单明细
     * @param id
     * @return
     */
    @GetMapping("/getEBookSimple/{id}")
    public Response<EBookSimpleVO> getEBookSimpleById(@PathVariable("id") Integer id) {
        return Response.success(eBookService.getEBookSimple(id));
    }

    /**
     * 根据id获取文章明细
     * @param id
     * @return
     */
    @GetMapping("/getEBookArticleById/{id}")
    public Response<EBookArticleVO> getEBookArticleById(@PathVariable("id") Integer id) {
        return Response.success(eBookService.getEBookArticleById(id));
    }

    /**
     * 新增文章
     * @return
     */
    @PostMapping("/article/add")
    public Response addArticle(@RequestBody EBookArticleForm form) {
        return Response.success(articleService.addArticle(form));
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @PostMapping("/article/delete/{id}")
    public Response deleteArticle(@PathVariable("id") Integer id) {
        return Response.success(articleService.deleteArticleById(id));
    }

    /**
     * 更新文章
     * @param id
     * @return
     */
    @PostMapping("/article/update/{id}")
    public Response updatedArticle(@PathVariable("id") Integer id, @RequestBody EBookArticleForm form) {
        return Response.success(articleService.updateArticle(id, form));
    }

    /**
     * 删除章节
     * @param id
     * @return
     */
    @PostMapping("/chapter/delete/{id}")
    public Response<Boolean> deleteChapter(@PathVariable("id") Integer id) {
        return Response.success(chapterService.deleteChapter(id));
    }

    /**
     * 新增章节
     * @return
     */
    @PostMapping("/chapter/add")
    public Response addChapter(@RequestBody EBookChapterForm form) {
        return Response.success(chapterService.addChapter(form));
    }

    /**
     * 根据id获取章节明细
     * @param id
     * @return
     */
    @GetMapping("/chapter/{id}")
    public Response<EBookChapterVO> getChapterById(@PathVariable("id") Integer id) {
        return Response.success(chapterService.getChapterById(id));
    }

    /**
     * 更新章节
     * @return
     */
    @PostMapping("/chapter/update/{id}")
    public Response<EBookChapterVO> updateChapter(@PathVariable("id") Integer id, @RequestBody EBookChapterForm form) {
        return Response.success(chapterService.updateChapter(id, form));
    }

    /**
     * 后台 返回分类列表
     * @return
     */
    @GetMapping("/category/list")
    public Response<List<CategoryVO>> getCategoryList() {
        return Response.success(eBookService.getCategoryList());
    }

    /**
     * 后台 分类明细
     * @param categoryId
     * @return
     */
    @GetMapping("/category/{id}")
    public Response<CategorySimpleVO> getCategoryDetail(@PathVariable("id") Integer categoryId) {
        return Response.success(eBookService.getCategoryDetail(categoryId));
    }

    /**
     * 删除分类
     * @param categoryId
     * @return
     */
    @PostMapping("/category/delete/{id}")
    public Response<Boolean> deleteCategory(@PathVariable("id") Integer categoryId) {
        return Response.success(eBookService.deleteCategory(categoryId));
    }

    /**
     * 更新分类
     * @param categoryId
     * @param form
     * @return
     */
    @PostMapping("/category/update/{id}")
    public Response<Boolean> updateCategory(@PathVariable("id") Integer categoryId, @RequestBody CategoryForm form) {
        return Response.success(eBookService.updateCategory(categoryId, form));
    }

    /**
     * 新增分类
     * @param form
     * @return
     */
    @PostMapping("/category/add")
    public Response<Boolean> addCategory(@RequestBody CategoryForm form) {
        return Response.success(eBookService.addCategory(form));
    }

    /**
     * 后台 获取电子书评论列表
     * @param pageNum
     * @param pageSize
     * @param cType
     * @param createdTime
     * @return
     */
    @GetMapping("/comments")
    public PageResult<BCommentVO> getEBookComments(@RequestParam("pageNum") Integer pageNum,
                                                   @RequestParam("pageSize") Integer pageSize,
                                                   @RequestParam("cType") String cType,
                                                   @RequestParam(value = "createdTIme", required = false) List<String> createdTime) {
        return PageResult.success(eBookService.getCommentList(pageNum, pageSize, cType, createdTime));
    }

    /**
     * 后台 根据id删除评论
     * @param id
     * @param cType
     * @return
     */
    @PostMapping("/comment/delete/{id}")
    public Response<Boolean> deleteBookComment(@PathVariable("id") Integer id, @RequestParam("cType") String cType) {
        return Response.success(eBookService.deleteComment(id, cType));
    }

    /**
     * 后台 批量删除电子书评论
     * @param form
     * @return
     */
    @PostMapping("/comment/delete/batch")
    public Response<Boolean> deleteBookCommentBatch(@RequestBody DelCommentBatchForm form) {
        return Response.success(eBookService.deleteCommentBatch(form));
    }

    /**
     * 后台 批量删除文章评论
     * @param form
     * @return
     */
    @PostMapping("/article/comment/delete/batch")
    public Response<Boolean> deleteArticleCommentBatch(@RequestBody DelCommentBatchForm form) {
        return Response.success(eBookService.deleteCommentBatch(form));
    }

    /**
     * 后台 返回文章评论列表
     * @param pageNum
     * @param pageSize
     * @param cType
     * @param createdTime
     * @return
     */
    @GetMapping("/article/comments")
    public PageResult<BCommentVO> getArticleComments(@RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("pageSize") Integer pageSize,
                                       @RequestParam("cType") String cType,
                                       @RequestParam(value = "createdTime", required = false) List<String> createdTime) {
        return PageResult.success(eBookService.getCommentList(pageNum, pageSize, cType, createdTime));
    }

    // ======================================================================================

    /**
     * 前台 电子书列表
     * @param pageNum
     * @param pageSize
     * @param scene
     * @param categoryId
     * @return
     */
    @GetMapping("/v2/book/list")
    public Response getBookList(@RequestParam("pageNum") Integer pageNum,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam("scene") String scene,
                                @RequestParam("categoryId") Integer categoryId) {
        return Response.success(eBookService.getBookList(pageNum, pageSize, scene, categoryId));
    }

    /**
     * 获取 某电子书 明细
     * @param id
     * @return
     */
    @GetMapping("/v2/book/{id}/detail")
    public Response<EBookFullVO> getBookDetailById(@PathVariable("id") Integer id) {
        return Response.success(eBookService.getBookDetailById(id));
    }

    /**
     * 获取某本电子书的评论
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/v2/book/{id}/comments")
    public Response<BCommentFullVO> getBookCommentsById(@PathVariable("id") Integer id,
                                                        @RequestParam("pageNum") Integer pageNum,
                                                        @RequestParam("pageSize") Integer pageSize) {
        return Response.success(eBookService.getBookComments(id, pageNum, pageSize));
    }

    /**
     * 查询某篇文章是否被当前用户收藏
     * @param id
     * @param type
     * @return
     */
    @GetMapping("/v2/book/collection/status")
    public Response<Boolean> checkBookThumbStatus(@RequestParam("id") Integer id, @RequestParam("type") String type) {
        return Response.success(eBookService.checkBookCollectionStatus(id, type));
    }

    /**
     * 点击电子书收藏
     * @param form
     * @return
     */
    @PostMapping("/v2/book/collection/hit")
    public Response<Boolean> hitBookCollectionIcon(@RequestBody CollectForm form) {
        return Response.success(eBookService.hitBookCollectionIcon(form));
    }

    /**
     * 新增电子书评论
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/v2/book/{id}/comment")
    public Response<Integer> addBookComment(@PathVariable("id") Integer id, @RequestBody AddCommentForm form) {
        return Response.success(eBookService.addBookComment(id, form));
    }

    /**
     * 阅读 某一篇文章
     * @param id
     * @return
     */
    @GetMapping("/v2/article/{id}/read")
    public Response readArticle(@PathVariable("id") Integer id) {
        return Response.success(eBookService.readArticle(id));
    }

    /**
     * 返回 某一篇文章 评论列表
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/v2/article/{id}/comments")
    public PageResult<CommentVO> getArticleComments(@PathVariable("id") Integer id,
                                                    @RequestParam("pageNum") Integer pageNum,
                                                    @RequestParam("pageSize") Integer pageSize,
                                                    @RequestParam("commentId") Integer commentId) {
        return PageResult.success(eBookService.getArticleComments(id, pageNum, pageSize, commentId));
    }

    /**
     * 新增文章评论
     * @param articleId
     * @param form
     * @return
     */
    @PostMapping("/v2/article/{id}/comment")
    public Response addArticleComment(@PathVariable("id") Integer articleId, @RequestBody AddCommentForm form) {
        return Response.success(eBookService.addArticleComment(articleId, form));
    }

    /**
     * 在学习电子书列表
     * @return
     */
    @GetMapping("/v2/study/list")
    public Response<List<StudyRecordVO>> queryStudyTopic() {
        return Response.success(eBookService.getStudyImageText());
    }

}
