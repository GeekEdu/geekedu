package com.zch.course.service.impl;

import com.zch.course.config.EsIndexInfo;
import com.zch.course.config.EsRestClient;
import com.zch.course.config.EsSearchRequest;
import com.zch.course.config.EsSourceData;
import com.zch.course.domain.repository.CourseInfoEs;
import com.zch.course.domain.repository.EsCourseFields;
import com.zch.course.service.EsCourseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author Poison02
 * @date 2024/3/22
 */
@Service
@Slf4j
public class EsCourseInfoServiceImpl implements EsCourseInfoService {
    @Override
    public boolean insert(CourseInfoEs courseInfoEs) {
        EsSourceData esSourceData = new EsSourceData();
        Map<String, Object> data = convert2EsSourceData(courseInfoEs);
        esSourceData.setDocId(courseInfoEs.getDocId().toString());
        esSourceData.setData(data);
        return EsRestClient.insertDoc(getEsIndexInfo(), esSourceData);
    }

    private Map<String, Object> convert2EsSourceData(CourseInfoEs subjectInfoEs) {
        Map<String, Object> data = new HashMap<>();
        data.put(EsCourseFields.COURSE_ID, subjectInfoEs.getCourseId());
        data.put(EsCourseFields.DOC_ID, subjectInfoEs.getDocId());
        data.put(EsCourseFields.TITLE, subjectInfoEs.getTitle());
        data.put(EsCourseFields.PRICE, subjectInfoEs.getPrice());
        data.put(EsCourseFields.DESCRIPTION, subjectInfoEs.getDescription());
        data.put(EsCourseFields.INTRO, subjectInfoEs.getIntro());
        data.put(EsCourseFields.COVER_LINK, subjectInfoEs.getCoverLink());
        data.put(EsCourseFields.GROUNDING_TIME, subjectInfoEs.getGroundingTime());
        data.put(EsCourseFields.CATEGORY_ID, subjectInfoEs.getCategoryId());
        data.put(EsCourseFields.CREATED_TIME, subjectInfoEs.getCreatedTime());
        data.put(EsCourseFields.UPDATED_TIME, subjectInfoEs.getUpdatedTime());
        return data;
    }

    @Override
    public List<CourseInfoEs> queryCourseInfoList(CourseInfoEs courseInfoEs) {
        EsSearchRequest esSearchRequest = createSearchListQuery(courseInfoEs);
        SearchResponse searchResponse = EsRestClient.searchWithTermQuery(getEsIndexInfo(), esSearchRequest);

        List<CourseInfoEs> subjectInfoEsList = new LinkedList<>();
        SearchHits searchHits = searchResponse.getHits();
        if (searchHits == null || searchHits.getHits() == null) {
            return subjectInfoEsList;
        }
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            CourseInfoEs subjectInfoEs = convertResult(hit);
            if (Objects.nonNull(subjectInfoEs)) {
                subjectInfoEsList.add(subjectInfoEs);
            }
        }
        return subjectInfoEsList;
    }

    private CourseInfoEs convertResult(SearchHit hit) {
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
        if (CollectionUtils.isEmpty(sourceAsMap)) {
            return null;
        }
        CourseInfoEs result = new CourseInfoEs();
        result.setCourseId(MapUtils.getInteger(sourceAsMap, EsCourseFields.COURSE_ID));
        result.setTitle(MapUtils.getString(sourceAsMap, EsCourseFields.TITLE));

        result.setDescription(MapUtils.getString(sourceAsMap, EsCourseFields.DESCRIPTION));

        result.setDocId(MapUtils.getLong(sourceAsMap, EsCourseFields.DOC_ID));
        result.setPrice(BigDecimal.valueOf(MapUtils.getDoubleValue(sourceAsMap, EsCourseFields.PRICE, 0.00)));
        result.setScore(new BigDecimal(String.valueOf(hit.getScore())).multiply(new BigDecimal("100.00")
                .setScale(2, RoundingMode.HALF_UP)));

        //处理title的高亮
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        HighlightField subjectNameField = highlightFields.get(EsCourseFields.TITLE);
        if(Objects.nonNull(subjectNameField)){
            Text[] fragments = subjectNameField.getFragments();
            StringBuilder subjectNameBuilder = new StringBuilder();
            for (Text fragment : fragments) {
                subjectNameBuilder.append(fragment);
            }
            result.setTitle(subjectNameBuilder.toString());
        }

        //处理描述高亮
        HighlightField subjectAnswerField = highlightFields.get(EsCourseFields.DESCRIPTION);
        if(Objects.nonNull(subjectAnswerField)){
            Text[] fragments = subjectAnswerField.getFragments();
            StringBuilder subjectAnswerBuilder = new StringBuilder();
            for (Text fragment : fragments) {
                subjectAnswerBuilder.append(fragment);
            }
            result.setDescription(subjectAnswerBuilder.toString());
        }

        return result;
    }

    private EsSearchRequest createSearchListQuery(CourseInfoEs req) {
        EsSearchRequest esSearchRequest = new EsSearchRequest();
        BoolQueryBuilder bq = new BoolQueryBuilder();
        MatchQueryBuilder subjectNameQueryBuilder =
                QueryBuilders.matchQuery(EsCourseFields.TITLE, req.getKeyword());

        bq.must(subjectNameQueryBuilder);
        subjectNameQueryBuilder.boost(2);

        MatchQueryBuilder subjectAnswerQueryBuilder =
                QueryBuilders.matchQuery(EsCourseFields.DESCRIPTION, req.getKeyword());
        bq.should(subjectAnswerQueryBuilder);

//        MatchQueryBuilder subjectTypeQueryBuilder =
//                QueryBuilders.matchQuery(EsCourseFields.INTRO, req.getKeyword());
//        bq.should(subjectTypeQueryBuilder);
        bq.minimumShouldMatch(1);

        HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
        highlightBuilder.preTags("<span style = \"color:red\">");
        highlightBuilder.postTags("</span>");

        esSearchRequest.setBq(bq);
        esSearchRequest.setHighlightBuilder(highlightBuilder);
        esSearchRequest.setFields(EsCourseFields.FIELD_QUERY);
        esSearchRequest.setNeedScroll(false);
        return esSearchRequest;
    }

    private EsIndexInfo getEsIndexInfo() {
        EsIndexInfo esIndexInfo = new EsIndexInfo();
        esIndexInfo.setClusterName("21389dddfe0e");
        esIndexInfo.setIndexName("course_index");
        return esIndexInfo;
    }
}
