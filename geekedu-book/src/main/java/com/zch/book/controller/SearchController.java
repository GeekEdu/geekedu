package com.zch.book.controller;

import com.zch.api.vo.system.search.SearchFullVO;
import com.zch.book.service.ISearchService;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Poison02
 * @date 2024/4/2
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final ISearchService searchService;

    /**
     * 全文检索 电子书或图文
     * @param offset
     * @param limit
     * @param type
     * @param keyword
     * @return
     */
    @GetMapping("/v2/search")
    public Response<SearchFullVO> esSearchBookOrTopic(@RequestParam("offset") Integer offset,
                                                 @RequestParam("limit") Integer limit,
                                                 @RequestParam("type") String type,
                                                 @RequestParam("keyword") String keyword) {
        return Response.success(searchService.search(offset, limit, type, keyword));
    }

}
