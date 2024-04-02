package com.zch.book.service.impl;

import com.zch.api.vo.system.search.SearchFullVO;
import com.zch.api.vo.system.search.SearchVO;
import com.zch.book.domain.dto.BookMSDTO;
import com.zch.book.domain.dto.TopicMSDTO;
import com.zch.book.service.ISearchService;
import com.zch.book.service.MeiliSearchService;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.meilisearch.result.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/4/2
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SearchServiceImpl implements ISearchService {

    private final MeiliSearchService meiliSearchService;

    @Override
    public SearchFullVO search(Integer offset, Integer limit, String type, String keyword) {
        SearchResult<BookMSDTO>  bookResult = meiliSearchService.bookSearch(offset, limit, keyword);
        SearchResult<TopicMSDTO> topicResult = meiliSearchService.topicSearch(offset, limit, keyword);
        SearchFullVO vo = new SearchFullVO();
        List<SearchVO> vos = new ArrayList<>();
        List<SearchVO> vos1 = new ArrayList<>();
        List<SearchVO> vos2 = new ArrayList<>();
        if (StringUtils.isNotBlank(type)) {
            if (ObjectUtils.isNotNull( bookResult) && ObjectUtils.isNotNull( bookResult.getHits())
                    && CollUtils.isNotEmpty( bookResult.getHits())) {
                 bookResult.getHits().forEach(item -> {
                    SearchVO vo1 = new SearchVO();
                    vo1.setId(item.getId());
                    vo1.setResourceId(item.getId());
                    vo1.setTitle(item.getName());
                    vo1.setResourceType("book");
                    vo1.setDescription(item.getShortDesc());
                    vos1.add(vo1);
                });
            }
            if (ObjectUtils.isNotNull(topicResult) && ObjectUtils.isNotNull(topicResult.getHits())
                    && CollUtils.isNotEmpty(topicResult.getHits())) {
                topicResult.getHits().forEach(item -> {
                    SearchVO vo1 = new SearchVO();
                    vo1.setId(item.getId());
                    vo1.setResourceId(item.getId());
                    vo1.setTitle(item.getTitle());
                    vo1.setResourceType("topic");
                    vo1.setDescription("");
                    vos2.add(vo1);
                });
            }
            if ("all".equals(type)) {
                vos.addAll(vos1);
                vos.addAll(vos2);
                vo.setData(vos);
                vo.setTotal((long) vos.size());
                return vo;
            } else if ("book".equals(type)) {
                vo.setData(vos1);
                vo.setTotal((long) vos1.size());
                return vo;
            } else if ("topic".equals(type)) {
                vo.setData(vos2);
                vo.setTotal((long) vos2.size());
                return vo;
            }
        }
        return vo;
    }
}
