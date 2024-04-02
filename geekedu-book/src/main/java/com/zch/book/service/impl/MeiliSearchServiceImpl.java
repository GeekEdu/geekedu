package com.zch.book.service.impl;

import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.zch.book.domain.dto.BookMSDTO;
import com.zch.book.domain.dto.TopicMSDTO;
import com.zch.book.mapper.BookMeiliSearchMapper;
import com.zch.book.mapper.TopicMeiliSearchMapper;
import com.zch.book.service.MeiliSearchService;
import com.zch.common.meilisearch.result.SearchResult;
import com.zch.common.meilisearch.service.IndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/4/2
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MeiliSearchServiceImpl implements MeiliSearchService {

    private final IndexService indexService;

    private final BookMeiliSearchMapper bookMeiliSearchMapper;

    private final TopicMeiliSearchMapper topicMeiliSearchMapper;

    @Override
    public void insertIndex(String uid, String primaryKey) {
        try {
            indexService.createIndex(uid, primaryKey);
        } catch (MeilisearchException e) {
            log.error("新增索引失败！" + e);
        }
    }

    @Override
    public void insertBookDocument(BookMSDTO book) {
        try {
            bookMeiliSearchMapper.update(book);
        } catch (MeilisearchException e) {
            log.error("新增电子书文档失败！" + e);
        }
    }

    @Override
    public void insertTopicDocument(TopicMSDTO topic) {
        try {
            topicMeiliSearchMapper.update(topic);
        } catch (MeilisearchException e) {
            log.error("新增图文文档失败！" + e);
        }
    }

    @Override
    public SearchResult<BookMSDTO> bookSearch(Integer offset, Integer limit, String keywords) {
        return bookMeiliSearchMapper.search(SearchRequest.builder()
                .q(keywords)
                .offset(offset)
                .limit(limit)
                .build());
    }

    @Override
    public SearchResult<TopicMSDTO> topicSearch(Integer offset, Integer limit, String keywords) {
        return topicMeiliSearchMapper.search(SearchRequest.builder()
                .q(keywords)
                .offset(offset)
                .limit(limit)
                .build());
    }
}
