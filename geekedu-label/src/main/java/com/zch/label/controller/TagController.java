package com.zch.label.controller;

import com.zch.api.dto.label.TagForm;
import com.zch.common.domain.query.PageQuery;
import com.zch.common.domain.vo.PageReqVO;
import com.zch.common.domain.vo.PageVO;
import com.zch.label.domain.dto.TagDTO;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.service.ITagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author Poison02
 * @date 2024/1/7
 */
@RestController
@Api(tags = "标签接口")
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final ITagService tagService;

    @ApiOperation("查找所有标签")
    @GetMapping("/getTagList")
    public PageVO<TagDTO> getTagList(@RequestBody PageReqVO req) {
        return tagService.getTagList(req);
    }

    @ApiOperation("根据条件查找标签")
    @GetMapping("/getTagByCondition")
    public PageVO<TagDTO> getTagByCondition(@RequestBody CategoryTagQuery query) {
        return tagService.getTagByCondition(query);
    }

    @ApiOperation("新增标签")
    @PostMapping("/add")
    public Tag addTag(@RequestBody TagForm form) {
        return tagService.addTag(form);
    }

    @ApiOperation("删除标签")
    @PostMapping("/delete")
    public Tag deleteTag(Long id) {
        return tagService.deleteTag(id);
    }

    @ApiOperation("修改标签")
    @PostMapping("/update")
    public Tag updateTag(@RequestBody TagForm form) {
        return tagService.updateTag(form);
    }

}
