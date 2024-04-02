package com.zch.book.service;

import com.zch.api.vo.system.search.SearchFullVO;

/**
 * @author Poison02
 * @date 2024/4/2
 */
public interface ISearchService {

    /**
     * 全文检索
     * @param offset
     * @param limit
     * @param type
     * @param keyword
     * @return
     */
    SearchFullVO search(Integer offset, Integer limit, String type, String keyword);

}
