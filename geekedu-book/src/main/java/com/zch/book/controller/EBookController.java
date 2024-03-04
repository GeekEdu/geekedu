package com.zch.book.controller;

import com.zch.api.dto.book.AddCommentForm;
import com.zch.api.dto.book.EBookArticleForm;
import com.zch.api.dto.book.EBookChapterForm;
import com.zch.api.dto.book.EBookForm;
import com.zch.api.vo.book.*;
import com.zch.api.vo.book.comment.BCommentFullVO;
import com.zch.book.service.IEBookArticleService;
import com.zch.book.service.IEBookChapterService;
import com.zch.book.service.IEBookService;
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
    public Response<Boolean> updateEBook(@PathVariable("id") Integer id, EBookForm form) {
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
     * 查询某篇文章是否点赞
     * @param id
     * @param type
     * @return
     */
    @GetMapping("/v2/book/thumb/status")
    public Response checkBookThumbStatus(@RequestParam("id") Integer id, @RequestParam("type") String type) {
        return Response.success();
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

}
