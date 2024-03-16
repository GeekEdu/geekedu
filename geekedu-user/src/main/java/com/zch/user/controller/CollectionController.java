package com.zch.user.controller;

import com.zch.api.dto.user.CollectForm;
import com.zch.common.mvc.result.Response;
import com.zch.user.service.ICollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/3/16
 */
@RestController
@RequestMapping("/api/collection")
@RequiredArgsConstructor
public class CollectionController {

    private final ICollectionService collectionService;

    /**
     * 查询是否收藏
     * @param bookId
     * @param type
     * @return
     */
    @GetMapping("/status")
    public Response<Boolean> checkCollectStatus(@RequestParam("id") Integer bookId, String type) {
        return Response.success(collectionService.checkCollectionStatus(bookId, type));
    }

    /**
     * 点击 抽藏图标
     * @param form
     * @return
     */
    @PostMapping("/hit")
    public Response<Boolean> hitCollectIcon(@RequestBody CollectForm form) {
        return Response.success(collectionService.hitCollectionIcon(form));
    }

}
