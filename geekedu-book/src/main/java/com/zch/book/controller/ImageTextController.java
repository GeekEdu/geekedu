package com.zch.book.controller;

import com.zch.api.dto.ask.ImageTextCommentForm;
import com.zch.api.dto.book.ImageTextForm;
import com.zch.api.vo.book.ImageTextAndCategoryVO;
import com.zch.api.vo.book.ImageTextSingleVO;
import com.zch.api.vo.book.ImageTextVO;
import com.zch.api.vo.book.comment.CommentVO;
import com.zch.book.service.IImageTextService;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/2/21
 */
@RestController
@RequestMapping("/api/imageText")
@RequiredArgsConstructor
public class ImageTextController {

    private final IImageTextService imageTextService;

    /**
     * 条件分页查找图文列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @return
     */
    @GetMapping("/getImageTextPageCondition")
    public Response<ImageTextAndCategoryVO> getListCondition(@RequestParam("pageNum") Integer pageNum,
                                                             @RequestParam("pageSize") Integer pageSize,
                                                             @RequestParam("sort") String sort,
                                                             @RequestParam("order") String order,
                                                             @RequestParam(value = "keywords", required = false) String keywords,
                                                             @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        return Response.success(imageTextService.getImageTextPageByCondition(pageNum, pageSize, sort, order, keywords, categoryId));
    }

    /**
     * 根据id获取图文明细
     * @param id
     * @return
     */
    @GetMapping("/getImageTextById/{id}")
    public Response<ImageTextVO> getImageTextById(@PathVariable("id") Integer id) {
        return Response.success(imageTextService.getImageTextById(id));
    }

    /**
     * 根据id删除图文
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public Response<Boolean> deleteImageTextById(@PathVariable("id") Integer id) {
        return Response.success(imageTextService.deleteImageTextById(id));
    }

    /**
     * 根据id更新图文
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/update/{id}")
    public Response<Boolean> updateImageTextById(@PathVariable("id") Integer id, @RequestBody ImageTextForm form) {
        return Response.success(imageTextService.updateImageTextById(id, form));
    }

    /**
     * 新增图文
     * @param form
     * @return
     */
    @PostMapping("/add")
    public Response<Boolean> addImageText(@RequestBody ImageTextForm form) {
        return Response.success(imageTextService.insertImageText(form));
    }

    //========================================================================================

    /**
     * 前台 图文列表
     * @param pageNum
     * @param pageSize
     * @param scene
     * @param categoryId
     * @return
     */
    @GetMapping("/v2/list")
    public Response<ImageTextAndCategoryVO> getImageTextList(@RequestParam("pageNum") Integer pageNum,
                                     @RequestParam("pageSize") Integer pageSize,
                                     @RequestParam("scene") String scene,
                                     @RequestParam("categoryId") Integer categoryId) {
        return Response.success(imageTextService.getImageTextList(pageNum, pageSize, scene, categoryId));
    }

    /**
     * 获取图文明细
     * @param id
     * @return
     */
    @GetMapping("/v2/{id}/detail")
    public Response<ImageTextSingleVO> getImageTextDetail(@PathVariable("id") Integer id) {
        return Response.success(imageTextService.getImageTextDetailById(id));
    }

    /**
     * 前台 获取图文评论列表
     * @param id
     * @return
     */
    @GetMapping("/v2/{id}/comments")
    public PageResult<CommentVO> getImageTextComments(@PathVariable("id") Integer id,
                                                      @RequestParam("pageNum") Integer pageNum,
                                                      @RequestParam("pageSize") Integer pageSize,
                                                      @RequestParam("commentId") Integer commentId) {
        return PageResult.success(imageTextService.getImageTextCommentList(id, pageNum, pageSize, commentId));
    }

    /**
     * 发表评论
     * @param id
     * @return
     */
    @PostMapping("/v2/{id}/comment")
    public Response<Integer> addCommentFirst(@PathVariable("id") Integer id, @RequestBody ImageTextCommentForm form) {
        return Response.success(imageTextService.addComment(id, form));
    }

}
