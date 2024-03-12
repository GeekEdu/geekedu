package com.zch.trade.controller;

import com.zch.api.dto.trade.GoodsForm;
import com.zch.api.vo.trade.creditmall.CreditFullVO;
import com.zch.api.vo.trade.creditmall.CreditMallVO;
import com.zch.api.vo.trade.creditmall.GoodsTypeVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.trade.service.ICreditMallService;
import com.zch.trade.service.IGoodsTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@RestController
@RequestMapping("/api/mall")
@RequiredArgsConstructor
public class CreditMallController {

    private final ICreditMallService creditMallService;

    private final IGoodsTypeService goodsTypeService;

    /**
     * 后台 返回积分商城列表
     * @param pageNum
     * @param PageSize
     * @param keywords
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Response<CreditFullVO> getCreditMallList(@RequestParam("pageNum") Integer pageNum,
                                                    @RequestParam("pageSize") Integer PageSize,
                                                    @RequestParam(value = "keywords", required = false) String keywords,
                                                    @RequestParam(value = "type", required = false) String type) {
        return Response.success(creditMallService.getMallListByCondition(pageNum, PageSize, keywords, type));
    }

    /**
     * 后台 获取商品分类列表
     * @return
     */
    @GetMapping("/goods/types")
    public Response<List<GoodsTypeVO>> getMallGoodsType() {
        return Response.success(goodsTypeService.getGoodsTypeList());
    }

    /**
     * 根据id删除商品
     * @param id
     * @return
     */
    @PostMapping("/good/{id}/delete")
    public Response<Boolean> deleteGoodById(@PathVariable("id") Integer id) {
        return Response.success(creditMallService.deleteGoodById(id));
    }

    /**
     * 商品明细
     * @param id
     * @return
     */
    @GetMapping("/good/{id}/detail")
    public Response<CreditMallVO> getGoodDetail(@PathVariable("id") Integer id) {
        return Response.success(creditMallService.getGoodDetailById(id));
    }

    /**
     * 新增商品
     * @param form
     * @return
     */
    @PostMapping("/good/add")
    public Response<Boolean> addGood(@RequestBody GoodsForm form) {
        return Response.success(creditMallService.addGood(form));
    }

    /**
     * 更新商品
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/good/{id}/update")
    public Response<Boolean> updateGood(@PathVariable("id") Integer id, @RequestBody GoodsForm form) {
        return Response.success(creditMallService.updateGood(id, form));
    }

    /**
     * 前台 返回积分商城列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/v2/list")
    public PageResult<CreditMallVO> getV2CreditMallList(@RequestParam("pageNum") Integer pageNum,
                                                        @RequestParam("pageSize") Integer pageSize) {
        return PageResult.success(creditMallService.getMallListByPage(pageNum, pageSize));
    }

}
