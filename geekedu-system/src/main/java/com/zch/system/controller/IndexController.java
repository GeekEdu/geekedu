package com.zch.system.controller;

import com.zch.api.vo.system.index.BlockVO;
import com.zch.common.mvc.result.Response;
import com.zch.system.service.IIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/18
 */
@RestController
@RequestMapping("/api/index")
@RequiredArgsConstructor
public class IndexController {

    private final IIndexService indexService;

    /**
     * 获取前台首页布局
     * @return
     */
    @GetMapping("/v2/block")
    public Response<List<BlockVO>> getV2IndexBlock() {
        return Response.success(indexService.getBlockList());
    }

}
