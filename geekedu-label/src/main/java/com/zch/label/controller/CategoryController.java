package com.zch.label.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zch.api.dto.label.CategoryForm;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.label.domain.po.Category;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.domain.vo.CategoryPageVO;
import com.zch.label.domain.vo.TagPageVO;
import com.zch.label.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author Poison02
 * @date 2024/1/7
 */
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/getCategoryList")
    public PageResult<CategoryPageVO> getCategoryList(@RequestBody CategoryTagQuery query) {
        Page<CategoryPageVO> result = categoryService.getCategoryList(query);
        return PageResult.success(result);
    }

    @GetMapping("/getCategoryByCondition")
    public PageResult<CategoryPageVO> getCategoryByCondition(@RequestBody CategoryTagQuery query) {
        Page<CategoryPageVO> result = categoryService.getCategoryByCondition(query);
        return PageResult.success(result);
    }

    @PostMapping("/add")
    public Response addCategory(@RequestBody CategoryForm form) {
        return Response.judge(categoryService.addCategory(form));
    }

    @PostMapping("/delete/{id}")
    public Response deleteCategory(@PathVariable("id") Long id) {
        return Response.judge(categoryService.deleteCategory(id));
    }

    @PostMapping("/update")
    public Response<Category> updateCategory(@RequestBody CategoryForm form) {
        return Response.success(categoryService.updateCategory(form));
    }

    @GetMapping("/getTagsByCategoryId")
    public PageResult<TagPageVO> getTagsByCategoryId(@RequestBody CategoryTagQuery query) {
        Page<TagPageVO> result = categoryService.getTagsCategoryId(query);
        return PageResult.success(result);
    }

}
