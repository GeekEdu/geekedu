package com.zch.exam.controller;

import com.zch.api.dto.exam.TagForm;
import com.zch.api.vo.exam.CTagsVO;
import com.zch.api.vo.exam.PaperAndCategoryVO;
import com.zch.api.vo.exam.PapersVO;
import com.zch.api.vo.exam.TagsVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.exam.service.IPapersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/27
 */
@RestController
@RequestMapping("/api/papers")
@RequiredArgsConstructor
public class PapersController {

    private final IPapersService papersService;

    /**
     * 条件分页查找试卷列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @return
     */
    @GetMapping("/getPapersPage")
    public Response<PaperAndCategoryVO> getPapersPage(@RequestParam("pageNum") Integer pageNum,
                                                      @RequestParam("pageSize") Integer pageSize,
                                                      @RequestParam("sort") String sort,
                                                      @RequestParam("order") String order,
                                                      @RequestParam(value = "keywords", required = false) String keywords,
                                                      @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        return Response.success(papersService.getPapersPage(pageNum, pageSize, sort, order, keywords, categoryId));
    }

    /**
     * 根据id查看试卷明细
     * @param id
     * @return
     */
    @GetMapping("/getPaperById/{id}")
    public Response<PapersVO> getPaperById(@PathVariable("id") Integer id) {
        return Response.success(papersService.getPaperById(id));
    }

    /**
     * 根据id删除试卷
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public Response<Boolean> deletePaperById(@PathVariable("id") Integer id) {
        return Response.success(papersService.deletePaperById(id));
    }

    /**
     * 返回简单分类列表
     * @return
     */
    @GetMapping("/sTag/list")
    public Response<List<CTagsVO>> getCategoryList() {
        return Response.success(papersService.getCategoryList());
    }

    /**
     * 分页查找分类列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/tag/list")
    public PageResult<TagsVO> getTagList(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize) {
        return PageResult.success(papersService.getTagList(pageNum, pageSize));
    }

    /**
     * 通过id查看分类明细
     * @param id
     * @return
     */
    @GetMapping("/tag/getTagById/{id}")
    public Response<CTagsVO> getTagById(@PathVariable("id") Integer id) {
        return Response.success(papersService.getTagById(id));
    }

    /**
     * 新增分类
     * @return
     */
    @PostMapping("/tag/add")
    public Response<Boolean> addTag(@RequestBody TagForm form) {
        return Response.success(papersService.addTag(form));
    }

    /**
     * 更新分类
     * @param id
     * @return
     */
    @PostMapping("/tag/update/{id}")
    public Response<Boolean> updateTag(@PathVariable("id") Integer id, @RequestBody TagForm form) {
        return Response.success(papersService.updateTag(id, form));
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @PostMapping("/tag/delete/{id}")
    public Response<Boolean> deleteTag(@PathVariable("id") Integer id) {
        return Response.success(papersService.deleteTag(id));
    }

}
