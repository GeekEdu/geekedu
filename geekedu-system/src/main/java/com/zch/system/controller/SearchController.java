package com.zch.system.controller;

import com.zch.api.vo.system.search.SearchFullVO;
import com.zch.common.mvc.result.Response;
import com.zch.system.service.ISearchService;
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

    private final ISearchService searchService;

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
    public Response<SearchFullVO> searchList(@RequestParam("pageNum") Integer pageNum,
                                              @RequestParam("pageSize") Integer pageSize,
                                              @RequestParam("type") String type,
                                              @RequestParam("keywords") String keywords) {
        return Response.success(searchService.search(pageNum, pageSize, type, keywords));
    }

}
