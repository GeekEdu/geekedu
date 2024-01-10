package com.zch.label.controller;

import com.zch.api.dto.label.TagForm;
import com.zch.common.domain.vo.PageReqVO;
import com.zch.common.domain.vo.PageVO;
import com.zch.label.domain.dto.TagDTO;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;
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
    public PageVO<TagDTO> getTagList(@RequestBody PageReqVO req) {
        return tagService.getTagList(req);
    }

    @GetMapping("/getTagByCondition")
    public PageVO<TagDTO> getTagByCondition(@RequestBody CategoryTagQuery query) {
        return tagService.getTagByCondition(query);
    }

    @PostMapping("/add")
    public Tag addTag(@RequestBody TagForm form) {
        return tagService.addTag(form);
    }

    @PostMapping("/delete/{id}")
    public Tag deleteTag(@PathVariable("id") Long id) {
        return tagService.deleteTag(id);
    }

    @PostMapping("/update")
    public Tag updateTag(@RequestBody TagForm form) {
        return tagService.updateTag(form);
    }

}
