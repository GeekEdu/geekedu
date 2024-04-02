package com.zch.system.service;

import com.zch.api.vo.system.search.SearchFullVO;

/**
 * @author Poison02
 * @date 2024/4/2
 */
public interface ISearchService {

    /**
     * 全文检索
     * @param pageNum
     * @param pageSize
     * @param type
     * @param keyword
     * @return
     */
    SearchFullVO search(Integer pageNum, Integer pageSize, String type, String keyword);

}
