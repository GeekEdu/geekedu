package com.zch.exam.controller;

import com.zch.api.vo.exam.CTagsVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.exam.service.IMockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@RestController
@RequestMapping("/api/mock")
@RequiredArgsConstructor
public class MockController {

    private final IMockService mockService;

    /**
     * 条件分页查找模拟考列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public PageResult getMockList(@RequestParam("pageNum") Integer pageNum,
                                  @RequestParam("pageSize") Integer pageSize,
                                  @RequestParam("sort") String sort,
                                  @RequestParam("order") String order,
                                  @RequestParam(value = "keywords", required = false) String keywords,
                                  @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        return PageResult.success(mockService.getMockPage(pageNum, pageSize, sort, order, keywords, categoryId));
    }

    /**
     * 根据id获取模拟考详情
     * @param id
     * @return
     */
    @GetMapping("/getMockById/{id}")
    public Response getMockById(@PathVariable("id") Integer id) {
        return Response.success(mockService.getMockById(id));
    }

    /**
     * 新增模拟考
     * @return
     */
    @PostMapping("/add")
    public Response<Boolean> addMock() {
        return Response.success();
    }

    /**
     * 更新模拟考
     * @param id
     * @return
     */
    @PostMapping("/update/{id}")
    public Response<Boolean> updateMock(@PathVariable("id") Integer id) {
        return Response.success();
    }

    /**
     * 根据id删除模拟考
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public Response<Boolean> deleteMock(@PathVariable("id") Integer id) {
        return Response.success();
    }

    /**
     * 模拟考分类列表
     * @param ids
     * @return
     */
    @GetMapping("/tag/list")
    public Response<List<CTagsVO>> getTagList(@RequestParam(value = "categoryIds", required = false) List<Integer> ids) {
        return Response.success(mockService.getTagList(ids));
    }

}
