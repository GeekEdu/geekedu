package com.zch.api.feignClient.book;

import com.zch.api.interceptor.FeignInterceptor;
import com.zch.api.vo.book.EBookSimpleVO;
import com.zch.api.vo.book.EBookVO;
import com.zch.api.vo.book.ImageTextSimpleVO;
import com.zch.api.vo.book.ImageTextVO;
import com.zch.api.vo.path.LearnPathVO;
import com.zch.api.vo.system.search.SearchFullVO;
import com.zch.common.mvc.result.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Poison02
 * @date 2024/3/4
 */
@FeignClient(contextId = "book", value = "book-service", configuration = FeignInterceptor.class)
public interface BookFeignClient {

    /**
     * 获取简单图文
     * @param id
     * @return
     */
    @GetMapping("/api/imageText/getSimpleImageText/{id}")
    public Response<ImageTextSimpleVO> getSimpleImageText(@PathVariable("id") Integer id);

    /**
     * 获取电子书简单明细
     * @param id
     * @return
     */
    @GetMapping("/api/eBook/getEBookSimple/{id}")
    Response<EBookSimpleVO> getEBookSimpleById(@PathVariable("id") Integer id);

    /**
     * 根据id获取电子书明细
     * @param id
     * @return
     */
    @GetMapping("/api/eBook/getEBookById/{id}")
    Response<EBookVO> getEBookById(@PathVariable("id") Integer id);

    /**
     * 根据id获取图文明细
     * @param id
     * @return
     */
    @GetMapping("/api/imageText/getImageTextById/{id}")
    Response<ImageTextVO> getImageTextById(@PathVariable("id") Integer id);

    /**
     * 获取学习路径详情
     * @param id
     * @return
     */
    @GetMapping("/api/path/{id}/detail")
    Response<LearnPathVO> getPathDetail(@PathVariable("id") Integer id);

    /**
     * 全文检索 电子书或图文
     * @param offset
     * @param limit
     * @param type
     * @param keyword
     * @return
     */
    @GetMapping("/api/search/v2/search")
    Response<SearchFullVO> esSearchBookOrTopic(@RequestParam("offset") Integer offset,
                                                      @RequestParam("limit") Integer limit,
                                                      @RequestParam("type") String type,
                                                      @RequestParam("keyword") String keyword);

}
