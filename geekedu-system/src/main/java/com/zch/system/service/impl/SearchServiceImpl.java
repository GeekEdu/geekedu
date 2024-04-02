package com.zch.system.service.impl;

import com.zch.api.feignClient.book.BookFeignClient;
import com.zch.api.feignClient.course.CourseFeignClient;
import com.zch.api.vo.system.search.SearchFullVO;
import com.zch.api.vo.system.search.SearchVO;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.Response;
import com.zch.system.service.ISearchService;
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

    private final CourseFeignClient courseFeignClient;

    private final BookFeignClient bookFeignClient;

    @Override
    public SearchFullVO search(Integer pageNum, Integer pageSize, String type, String keyword) {
        pageNum = pageNum < 1 ? 1 : pageNum;
        if (pageSize < 1) pageSize = 1;
        Integer offset = (pageNum - 1) * pageSize;
        Integer limit = Math.min(pageSize, 100);
        SearchFullVO vo = new SearchFullVO();
        if (StringUtils.isNotBlank(type)) {
            if ("all".equals(type)) {
                Response<SearchFullVO> res1 = courseFeignClient.searchCourse(offset, limit, type, keyword);
                Response<SearchFullVO> res2 = bookFeignClient.searchBookOrTopic(offset, limit, type, keyword);
                if (ObjectUtils.isNotNull(res1) && ObjectUtils.isNotNull(res1.getData())
                && ObjectUtils.isNotNull(res2) && ObjectUtils.isNotNull(res2.getData())) {
                    List<SearchVO> vos = new ArrayList<>();
                    vos.addAll(res1.getData().getData());
                    vos.addAll(res2.getData().getData());
                    vo.setData(vos);
                    vo.setTotal((long) vos.size());
                    return vo;
                }
            } else if ("vod".equals(type) || "live".equals(type)) {
                Response<SearchFullVO> res1 = courseFeignClient.searchCourse(offset, limit, type, keyword);
                if (ObjectUtils.isNotNull(res1) && ObjectUtils.isNotNull(res1.getData())) {
                    List<SearchVO> vos = new ArrayList<>(res1.getData().getData());
                    vo.setData(vos);
                    vo.setTotal((long) vos.size());
                    return vo;
                }
            }  else if ("book".equals(type) || "topic".equals(type)) {
                Response<SearchFullVO> res = bookFeignClient.searchBookOrTopic(offset, limit, type, keyword);
                if (ObjectUtils.isNotNull(res) && ObjectUtils.isNotNull(res.getData())) {
                    List<SearchVO> vos = new ArrayList<>(res.getData().getData());
                    vo.setData(vos);
                    vo.setTotal((long) vos.size());
                    return vo;
                }
            }
        }
        return vo;
    }
}
