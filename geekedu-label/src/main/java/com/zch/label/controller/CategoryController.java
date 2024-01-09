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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author Poison02
 * @date 2024/1/7
 */
@RestController
@RequestMapping("/api/category")
@Api(tags = "分类接口")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @ApiOperation("查找所有分类")
    @GetMapping("/getCategoryList")
    public PageVO<CategoryDTO> getCategoryList(@RequestBody PageReqVO req) {
        return categoryService.getCategoryList(req);
    }

    @ApiOperation("条件查找分类")
    @GetMapping("/getCategoryByCondition")
    public PageVO<CategoryDTO> getCategoryByCondition(@RequestBody CategoryTagQuery query) {
        return categoryService.getCategoryByCondition(query);
    }

    @ApiOperation("新增分类")
    @PostMapping("/add")
    public Category addCategory(@RequestBody CategoryForm form) {
        return categoryService.addCategory(form);
    }

    @ApiOperation("根据分类id删除分类")
    @PostMapping("/delete/{id}")
    public Category deleteCategory(@PathVariable("id") Long id) {
        return categoryService.deleteCategory(id);
    }

    @ApiOperation("更新分类信息")
    @PostMapping("/update")
    public Category updateCategory(@RequestBody CategoryForm form) {
        return categoryService.updateCategory(form);
    }

    @ApiOperation("查询分类id下的所有标签")
    @GetMapping("/getTagsByCategoryId")
    public PageVO<TagDTO> getTagsByCategoryId(@RequestBody CategoryReqVO req) {
        return categoryService.getTagsCategoryId(req);
    }

}
