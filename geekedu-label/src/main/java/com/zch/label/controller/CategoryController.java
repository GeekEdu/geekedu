package com.zch.label.controller;

import com.zch.api.dto.label.CategoryForm;
import com.zch.common.domain.Response;
import com.zch.label.domain.dto.CategoryDTO;
import com.zch.label.domain.dto.TagDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/7
 */
@RestController
@RequestMapping("/api/category")
@Api(tags = "分类接口")
@RequiredArgsConstructor
public class CategoryController {

    @ApiOperation("查找所有分类")
    @GetMapping("/getCategoryList")
    public Response<List<CategoryDTO>> getCategoryList() {
        // TODO
        return null;
    }

    @ApiOperation("条件查找分类")
    @GetMapping("/getCategoryByCondition")
    public Response<List<CategoryDTO>> getCategoryByCondition() {
        // TODO
        return null;
    }

    @ApiOperation("新增分类")
    @PostMapping("/add")
    public Response addCategory(@RequestBody CategoryForm form) {
        // TODO
        return null;
    }

    @ApiOperation("根据分类id删除分类")
    @PostMapping("/delete")
    public Response deleteCategory(Long id) {
        // TODO
        return null;
    }

    @ApiOperation("更新分类信息")
    @PostMapping("/update")
    public Response updateCategory(@RequestBody CategoryForm form) {
        // TODO
        return null;
    }

    @ApiOperation("查询分类id下的所有标签")
    @GetMapping("/getTagsByCategoryId")
    public Response<List<TagDTO>> getTagsByCategoryId(Long id) {
        // TODO
        return null;
    }

}
