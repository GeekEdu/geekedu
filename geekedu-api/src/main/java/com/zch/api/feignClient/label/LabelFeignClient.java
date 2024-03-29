package com.zch.api.feignClient.label;

import com.zch.api.dto.label.CategoryForm;
import com.zch.api.interceptor.FeignInterceptor;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.label.CategoryVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/9
 */
@FeignClient(contextId = "label", value = "label-service", configuration = FeignInterceptor.class)
public interface LabelFeignClient {
    /**
     * 分页返回分类列表
     * @param pageNum
     * @param pageSize
     * @param type 分类类型
     * @return
     */
    @GetMapping("/api/category/courseCategory")
    public PageResult<CategoryVO> getCategory(@RequestParam("pageNum") Integer pageNum,
                                                 @RequestParam("pageSize") Integer pageSize,
                                                 @RequestParam("type") String type);

    /**
     * 新增分类
     * @param form
     * @return
     */
    @PostMapping("/api/category/add")
    public Response<Boolean> addCategory(@RequestBody CategoryForm form);

    /**
     * 根据id和类型查找分类
     * @param id
     * @return
     */
    @GetMapping("/api/category/getCategoryById")
    public Response<CategorySimpleVO> getCategoryById(@RequestParam("id") Integer id, @RequestParam("type") String type);

    /**
     * 查找分类列表
     * @param type
     * @return
     */
    @GetMapping("/api/category/getCategoryList")
    public Response<List<CategoryVO>> getCategoryList(@RequestParam("type") String type);

    /**
     * 查找简单分类列表
     * @param type
     * @return
     */
    @GetMapping("/api/category/getCategorySimpleList")
    Response<List<CategorySimpleVO>> getCategorySimpleList(@RequestParam("type") String type);

    /**
     * 更新分类
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/api/category/update/{id}")
    public Response<Boolean> updateCategoryById(@PathVariable("id") Integer id, @RequestBody CategoryForm form);

    /**
     * 删除分类
     * @param id
     * @return
     */
    @PostMapping("/api/category/delete/{id}")
    public Response<Boolean> deleteCategoryById(@PathVariable("id") Integer id);
}
