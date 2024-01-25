package com.zch.label.controller;

import com.zch.api.dto.label.CategoryForm;
import com.zch.common.mvc.result.Response;
import com.zch.label.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

}
