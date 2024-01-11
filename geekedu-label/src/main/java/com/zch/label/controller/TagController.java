package com.zch.label.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zch.api.dto.label.TagForm;
import com.zch.common.domain.result.PageResult;
import com.zch.common.domain.result.Response;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.domain.vo.TagPageVO;
import com.zch.label.service.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author Poison02
 * @date 2024/1/7
 */
@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final ITagService tagService;

    @GetMapping("/getTagList")
    public PageResult<TagPageVO> getTagList(@RequestBody CategoryTagQuery query) {
        Page<TagPageVO> result = tagService.getTagList(query);
        return PageResult.success(result);
    }

    @GetMapping("/getTagByCondition")
    public PageResult<TagPageVO> getTagByCondition(@RequestBody CategoryTagQuery query) {
        Page<TagPageVO> result = tagService.getTagByCondition(query);
        return PageResult.success(result);
    }

    @PostMapping("/add")
    public Response addTag(@RequestBody TagForm form) {
        return Response.judge(tagService.addTag(form));
    }

    @PostMapping("/delete/{id}")
    public Response deleteTag(@PathVariable("id") Long id) {
        return Response.judge(tagService.deleteTag(id));
    }

    @PostMapping("/update")
    public Response<Tag> updateTag(@RequestBody TagForm form) {
        return Response.success(tagService.updateTag(form));
    }

}
