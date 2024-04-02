package com.zch.course.service.impl;

import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.zch.common.meilisearch.result.SearchResult;
import com.zch.common.meilisearch.service.IndexService;
import com.zch.course.domain.dto.CourseMSDTO;
import com.zch.course.domain.dto.LiveCourseMSDTO;
import com.zch.course.mapper.MSCourseMapper;
import com.zch.course.mapper.MSLiveCourseMapper;
import com.zch.course.service.MeiliSearchService;
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

    private final MSCourseMapper msCourseMapper;

    private final MSLiveCourseMapper msLiveCourseMapper;

    @Override
    public void insertCourseIndex(String uid, String primaryKey) {
        try {
            indexService.createIndex(uid, primaryKey);
        } catch (MeilisearchException e) {
            log.error("新增索引失败！" + e);
        }
    }

    @Override
    public void insertCourseDocument(CourseMSDTO course) {
        try {
            msCourseMapper.update(course);
        } catch (MeilisearchException e) {
            log.error("新增录播课文档失败！" + e);
        }
    }

    @Override
    public void insertLiveCourseDocument(LiveCourseMSDTO course) {
        try {
            msLiveCourseMapper.update(course);
        } catch (MeilisearchException e) {
            log.error("新增直播课文档失败！" + e);
        }
    }

    @Override
    public SearchResult<CourseMSDTO> courseSearch(Integer offset, Integer limit, String keywords) {
        return msCourseMapper.search(SearchRequest.builder()
                .q(keywords)
                .offset(offset)
                .limit(limit)
                .build());
    }

    @Override
    public SearchResult<LiveCourseMSDTO> liveCourseSearch(Integer offset, Integer limit, String keywords) {
        return msLiveCourseMapper.search(SearchRequest.builder()
                .q(keywords)
                .offset(offset)
                .limit(limit)
                .build());
    }
}
