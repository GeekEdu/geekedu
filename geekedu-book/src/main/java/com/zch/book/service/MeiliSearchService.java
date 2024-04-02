package com.zch.book.service;

import com.zch.book.domain.dto.BookMSDTO;
import com.zch.book.domain.dto.TopicMSDTO;
import com.zch.common.meilisearch.result.SearchResult;

/**
 * @author Poison02
 * @date 2024/4/2
 */
public interface MeiliSearchService {

    /**
     * 新增 Index
     * @param uid
     * @param primaryKey
     */
    void insertIndex(String uid, String primaryKey);

    /**
     * 新增电子书文档
     * @param book
     */
    void insertBookDocument(BookMSDTO book);

    /**
     * 新增图文文档
     * @param topic
     */
    void insertTopicDocument(TopicMSDTO topic);

    /**
     * 电子书检索
     * @param offset
     * @param limit
     * @param keywords
     * @return
     */
    SearchResult<BookMSDTO> bookSearch(Integer offset, Integer limit, String keywords);

    /**
     * 图文检索
     * @param offset
     * @param limit
     * @param keywords
     * @return
     */
    SearchResult<TopicMSDTO> topicSearch(Integer offset, Integer limit, String keywords);

}
