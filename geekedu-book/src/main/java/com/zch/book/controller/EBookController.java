package com.zch.book.controller;

import com.zch.api.vo.book.EBookAndCategoryVO;
import com.zch.book.service.IEBookService;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
