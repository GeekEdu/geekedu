package com.zch.label.controller;

import com.zch.api.dto.label.CategoryForm;
import com.zch.common.domain.vo.PageReqVO;
import com.zch.common.domain.vo.PageVO;
import com.zch.label.domain.dto.CategoryDTO;
import com.zch.label.domain.dto.TagDTO;
import com.zch.label.domain.po.Category;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.domain.vo.CategoryReqVO;
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
    public PageVO<CategoryDTO> getCategoryList(@RequestBody PageReqVO req) {
        return categoryService.getCategoryList(req);
    }

    @GetMapping("/getCategoryByCondition")
    public PageVO<CategoryDTO> getCategoryByCondition(@RequestBody CategoryTagQuery query) {
        return categoryService.getCategoryByCondition(query);
    }

    @PostMapping("/add")
    public Category addCategory(@RequestBody CategoryForm form) {
        return categoryService.addCategory(form);
    }

    @PostMapping("/delete/{id}")
    public Category deleteCategory(@PathVariable("id") Long id) {
        return categoryService.deleteCategory(id);
    }

    @PostMapping("/update")
    public Category updateCategory(@RequestBody CategoryForm form) {
        return categoryService.updateCategory(form);
    }

    @GetMapping("/getTagsByCategoryId")
    public PageVO<TagDTO> getTagsByCategoryId(@RequestBody CategoryReqVO req) {
        return categoryService.getTagsCategoryId(req);
    }

}
