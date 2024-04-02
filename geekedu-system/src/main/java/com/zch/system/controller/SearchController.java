package com.zch.system.controller;

import com.zch.api.feignClient.book.BookFeignClient;
import com.zch.api.feignClient.course.CourseFeignClient;
import com.zch.api.vo.system.search.SearchVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Poison02
 * @date 2024/3/11
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final CourseFeignClient courseFeignClient;

    private final BookFeignClient bookFeignClient;

    /**
     * 全文检索
     *
     * @param pageNum
     * @param pageSize
     * @param type     检索类型 all vod live topic book
     * @param keywords 关键字
     * @return
     */
    @GetMapping("/list")
    public Response<PageResult.Data<SearchVO>> searchList(@RequestParam("pageNum") Integer pageNum,
                                                          @RequestParam("pageSize") Integer pageSize,
                                                          @RequestParam("type") String type,
                                                          @RequestParam("keywords") String keywords) {
        pageNum = pageNum < 1 ? 1 : pageNum;
        if (pageSize < 1) pageSize = 1;
        Integer offset = (pageNum - 1) * pageSize;
        Integer limit = Math.min(pageSize, 100);
        return Response.success();
    }

}
