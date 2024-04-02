package com.zch.course.service;

import com.zch.common.meilisearch.result.SearchResult;
import com.zch.course.domain.dto.CourseMSDTO;
import com.zch.course.domain.dto.LiveCourseMSDTO;

/**
 * @author Poison02
 * @date 2024/4/2
 */
public interface MeiliSearchService {

    /**
     * 新增课程 Index
     * @param uid
     * @param primaryKey
     */
    void insertCourseIndex(String uid, String primaryKey);

    /**
     * 新增录播课文档
     * @param course
     */
    void insertCourseDocument(CourseMSDTO course);

    /**
     * 新增直播课文档
     * @param course
     */
    void insertLiveCourseDocument(LiveCourseMSDTO course);

    /**
     * 录播课检索
     * @param offset
     * @param limit
     * @param keywords
     * @return
     */
    SearchResult<CourseMSDTO> courseSearch(Integer offset, Integer limit, String keywords);

    /**
     * 直播课检索
     * @param offset
     * @param limit
     * @param keywords
     * @return
     */
    SearchResult<LiveCourseMSDTO> liveCourseSearch(Integer offset, Integer limit, String keywords);

}
