package com.zch.label.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zch.api.dto.label.CategoryForm;
import com.zch.api.vo.label.CategoryVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
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

    @PostMapping("/add")
    public Response addCategory(@RequestBody CategoryForm form) {
        return Response.success(categoryService.addCategory(form));
    }

    @GetMapping("/courseCategory")
    public PageResult<CategoryVO> courseCategory(@RequestParam("pageNum") Integer pageNum,
                                                 @RequestParam("pageSize") Integer pageSize,
                                                 @RequestParam("type") String type) {
        Page<CategoryVO> result = categoryService.getCourseCategory(pageNum, pageSize, type);
        return PageResult.success(result);
    }

}
