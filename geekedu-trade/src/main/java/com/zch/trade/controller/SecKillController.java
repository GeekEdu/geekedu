package com.zch.trade.controller;

import com.zch.api.vo.trade.seckill.SecondKillVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.trade.service.ISecondKillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
