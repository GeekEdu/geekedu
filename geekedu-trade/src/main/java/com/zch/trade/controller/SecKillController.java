package com.zch.trade.controller;

import com.zch.api.dto.trade.seckill.CaptchaForm;
import com.zch.api.dto.trade.seckill.SecKillForm;
import com.zch.api.vo.trade.seckill.SecKillV2VO;
import com.zch.api.vo.trade.seckill.SecondKillVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.trade.service.ISecondKillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/3/25
 */
@RestController
@RequestMapping("/api/seckill")
@RequiredArgsConstructor
public class SecKillController {

    private final ISecondKillService secondKillService;

    /**
     * 秒杀列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @return
     */
    @GetMapping("/list")
    public PageResult<SecondKillVO> querySecKillList(@RequestParam("pageNum") Integer pageNum,
                                                     @RequestParam("pageSize") Integer pageSize,
                                                     @RequestParam(value = "sort", required = false) String sort,
                                                     @RequestParam(value = "order", required = false) String order,
                                                     @RequestParam(value = "keywords", required = false) String keywords) {
        return PageResult.success(secondKillService.querySecKillList(pageNum, pageSize, sort, order, keywords));
    }

    /**
     * 秒杀详情
     * @param id
     * @return
     */
    @GetMapping("/{id}/detail")
    public Response<SecondKillVO> querySecKillDetail(@PathVariable("id") Integer id) {
        return Response.success(secondKillService.querySecKillDetail(id));
    }

    /**
     * 更新秒杀
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/{id}/update")
    public Response<Boolean> updateSecKill(@PathVariable("id") Integer id, @RequestBody SecKillForm form) {
        return Response.success(secondKillService.updateSecKill(id, form));
    }

    /**
     * 删除秒杀
     * @param id
     * @return
     */
    @PostMapping("/{id}/delete")
    public Response<Boolean> deleteSecKill(@PathVariable("id") Integer id) {
        return Response.success(secondKillService.deleteSecKill(id));
    }

    /**
     * 新增秒杀
     * @param form
     * @return
     */
    @PostMapping("/add")
    public Response<Boolean> addSecKill(@RequestBody SecKillForm form) {
        return Response.success(secondKillService.addSecKill(form));
    }

    //==========================================================================================

    /**
     * 前台秒杀详情
     * @param goodsId
     * @param goodsType
     * @return
     */
    @GetMapping("/v2/detail")
    public Response<SecKillV2VO> getV2Detail(@RequestParam("goodsId") Integer goodsId,
                                             @RequestParam("goodsType") String goodsType) {
        return Response.success(secondKillService.getV2Detail(goodsId, goodsType));
    }

    /**
     * 开始秒杀
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/v2/{id}/start")
    public Response<String> startSecKill(@PathVariable("id") Integer id,
                                         @RequestBody CaptchaForm form) {
        return Response.success();
    }

}
