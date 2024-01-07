package com.zch.label.controller;

import com.zch.api.dto.label.TagForm;
import com.zch.common.domain.Response;
import com.zch.label.domain.dto.TagDTO;
import com.zch.label.domain.query.CategoryTagQuery;
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
@Api(tags = "标签接口")
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    @ApiOperation("查找所有标签")
    @GetMapping("/getTagList")
    public Response<List<TagDTO>> getTagList() {
        // TODO
        return null;
    }

    @ApiOperation("根据条件查找标签")
    @GetMapping("/getTagByCondition")
    public Response<List<TagDTO>> getTagByCondition(@RequestBody CategoryTagQuery query) {
        // TODO
        return null;
    }

    @ApiOperation("新增标签")
    @GetMapping("/add")
    public Response addTag(@RequestBody TagForm form) {
        // TODO
        return null;
    }

    @ApiOperation("删除标签")
    @PostMapping("/delete")
    public Response deleteTag(Long id) {
        // TODO
        return null;
    }

    @ApiOperation("修改标签")
    @PostMapping("/update")
    public Response updateTag(@RequestBody TagForm form) {
        // TODO
        return null;
    }

}
