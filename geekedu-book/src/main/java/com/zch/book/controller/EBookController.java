package com.zch.book.controller;

import com.zch.api.dto.book.EBookForm;
import com.zch.api.vo.book.EBookAndCategoryVO;
import com.zch.api.vo.book.EBookArticleFullVO;
import com.zch.api.vo.book.EBookChapterVO;
import com.zch.api.vo.book.EBookVO;
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

}
