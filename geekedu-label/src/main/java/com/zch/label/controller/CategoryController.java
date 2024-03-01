package com.zch.label.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zch.api.dto.label.CategoryForm;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.label.CategoryVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.label.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Poison02
 * @date 2024/1/7
 */
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    /**
     * 新增分类
     * @param form
     * @return
     */
    @PostMapping("/add")
    public Response addCategory(@RequestBody CategoryForm form) {
        return Response.success(categoryService.addCategory(form));
    }

    /**
     * 分页查找分类列表
     * @param pageNum
     * @param pageSize
     * @param type
     * @return
     */
    @GetMapping("/getCategoryPage")
    public PageResult<CategoryVO> getCategory(@RequestParam("pageNum") Integer pageNum,
                                                 @RequestParam("pageSize") Integer pageSize,
                                                 @RequestParam("type") String type) {
        Page<CategoryVO> result = categoryService.getCategoryPage(pageNum, pageSize, type);
        return PageResult.success(result);
    }

    /**
     * 根据分类id查找分类
     * @param id
     * @param type
     * @return
     */
    @GetMapping("/getCategoryById")
    public Response<CategorySimpleVO> getCategoryById(@RequestParam("id") Integer id, @RequestParam("type") String type) {
        return Response.success(categoryService.getCategoryById(id, type));
    }

    /**
     * 返回分类列表 不分页
     * @param type
     * @return
     */
    @GetMapping("/getCategoryList")
    public Response<List<CategoryVO>> getCategoryList(@RequestParam("type") String type) {
        return Response.success(categoryService.getCategory(type));
    }

    /**
     * 返回简单分类列表 不分页
     * @param type
     * @return
     */
    @GetMapping("/getCategorySimpleList")
    public Response<List<CategorySimpleVO>> getCategorySimpleList(@RequestParam("type") String type) {
        return Response.success(categoryService.getCategorySimpleList(type));
    }

    /**
     * 根据分类id更新分类
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/update/{id}")
    public Response<Boolean> updateCategoryById(@PathVariable("id") Integer id, @RequestBody CategoryForm form) {
        return Response.success(categoryService.updateCategory(id, form));
    }

    /**
     * 根据分类id删除分类
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public Response<Boolean> deleteCategoryById(@PathVariable("id") Integer id) {
        return Response.success(categoryService.deleteCategory(id));
    }

}
