package com.zch.book.controller;

import com.zch.api.dto.path.LearnPathForm;
import com.zch.api.dto.path.StepForm;
import com.zch.api.vo.path.LearnPathVO;
import com.zch.api.vo.path.StepEndVO;
import com.zch.api.vo.path.StepVO;
import com.zch.book.service.ILearnPathService;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/13
 */
@RestController
@RequestMapping("/api/path")
@RequiredArgsConstructor
public class LearnPathController {

    private final ILearnPathService learnPathService;

    /**
     * 获取学习路径列表
     * @param pageNum
     * @param pageSize
     * @param keywords
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public PageResult<LearnPathVO> getPathList(@RequestParam("pageNum") Integer pageNum,
                                               @RequestParam("pageSize") Integer pageSize,
                                               @RequestParam(value = "keywords", required = false) String keywords,
                                               @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        return PageResult.success(learnPathService.getPathList(pageNum, pageSize, keywords, categoryId));
    }

    /**
     * 获取学习路径详情
     * @param id
     * @return
     */
    @GetMapping("/{id}/detail")
    public Response<LearnPathVO> getPathDetail(@PathVariable("id") Integer id) {
        return Response.success(learnPathService.getPathDetail(id));
    }

    /**
     * 删除学习路径
     * @param id
     * @return
     */
    @PostMapping("/{id}/delete")
    public Response<Boolean> deletePath(@PathVariable("id") Integer id) {
        return Response.success(learnPathService.deletePath(id));
    }

    /**
     * 更新学习路径
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/{id}/update")
    public Response<Boolean> updatePath(@PathVariable("id") Integer id, @RequestBody LearnPathForm form) {
        return Response.success(learnPathService.updatePath(id, form));
    }

    /**
     * 新增学习路径
     * @param form
     * @return
     */
    @PostMapping("/add")
    public Response<Boolean> addPath(@RequestBody LearnPathForm form) {
        return Response.success(learnPathService.addPath(form));
    }

    /**
     * 路径下 步骤列表
     * @param id
     * @return
     */
    @GetMapping("/{id}/step/list")
    public Response<List<StepVO>> getStepList(@PathVariable("id") Integer id) {
        return Response.success(learnPathService.getStepList(id));
    }

    /**
     * 删除步骤
     * @param stepId
     * @return
     */
    @PostMapping("/step/{id}/delete")
    public Response<Boolean> deleteStepById(@PathVariable("id") Integer stepId) {
        return Response.success(learnPathService.deleteStepById(stepId));
    }

    /**
     * 获取步骤详情
     * @param id
     * @return
     */
    @GetMapping("/step/{id}/detail")
    public Response<StepEndVO> getStepDetail(@PathVariable("id") Integer id) {
        return Response.success(learnPathService.getStepDetail(id));
    }

    /**
     * 更新步骤
     * @param stepId
     * @param form
     * @return
     */
    @PostMapping("/step/{id}/update")
    public Response<Boolean> updateStep(@PathVariable("id") Integer stepId, @RequestBody StepForm form) {
        return Response.success(learnPathService.updateStep(stepId, form));
    }

    /**
     * 新增步骤
     * @param form
     * @return
     */
    @PostMapping("/step/add")
    public Response<Boolean> addStep(@RequestBody StepForm form) {
        return Response.success(learnPathService.addStep(form));
    }

}
