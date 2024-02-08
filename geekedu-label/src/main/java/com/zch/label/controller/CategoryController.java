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

    @PostMapping("/add")
    public Response addCategory(@RequestBody CategoryForm form) {
        return Response.success(categoryService.addCategory(form));
    }

    @GetMapping("/getCategoryPage")
    public PageResult<CategoryVO> getCategory(@RequestParam("pageNum") Integer pageNum,
                                                 @RequestParam("pageSize") Integer pageSize,
                                                 @RequestParam("type") String type) {
        Page<CategoryVO> result = categoryService.getCategoryPage(pageNum, pageSize, type);
        return PageResult.success(result);
    }

    @GetMapping("/getCategoryById")
    public Response<CategorySimpleVO> getCategoryById(@RequestParam("id") Integer id, @RequestParam("type") String type) {
        return Response.success(categoryService.getCategoryById(id, type));
    }

    @GetMapping("/getCategoryList")
    public Response<List<CategoryVO>> getCategoryList(@RequestParam("type") String type) {
        return Response.success(categoryService.getCategory(type));
    }

    @PostMapping("/update/{id}")
    public Response<Boolean> updateCategoryById(@PathVariable("id") Integer id, @RequestBody CategoryForm form) {
        return Response.success(categoryService.updateCategory(id, form));
    }

    @PostMapping("/delete/{id}")
    public Response<Boolean> deleteCategoryById(@PathVariable("id") Integer id) {
        return Response.success(categoryService.deleteCategory(id));
    }

}
