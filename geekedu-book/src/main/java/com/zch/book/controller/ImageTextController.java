package com.zch.book.controller;

import com.zch.api.vo.book.ImageTextAndCategoryVO;
import com.zch.api.vo.book.ImageTextVO;
import com.zch.book.service.IImageTextService;
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

    @GetMapping("/getImageTextPageCondition")
    public Response<ImageTextAndCategoryVO> getListCondition(@RequestParam("pageNum") Integer pageNum,
                                                             @RequestParam("pageSize") Integer pageSize,
                                                             @RequestParam("sort") String sort,
                                                             @RequestParam("order") String order,
                                                             @RequestParam(value = "keywords", required = false) String keywords,
                                                             @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        return Response.success(imageTextService.getImageTextPageByCondition(pageNum, pageSize, sort, order, keywords, categoryId));
    }

    @GetMapping("/getImageTextById/{id}")
    public Response<ImageTextVO> getImageTextById(@PathVariable("id") Integer id) {
        return Response.success(imageTextService.getImageTextById(id));
    }

}
