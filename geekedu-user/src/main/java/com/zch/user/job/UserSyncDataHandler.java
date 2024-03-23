package com.zch.user.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.zch.user.service.ICollectionService;
import com.zch.user.service.IThumbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Poison02
 * @date 2024/3/23
 */
@Component
@Slf4j
public class UserSyncDataHandler {

    @Resource
    private IThumbService thumbService;

    @Resource
    private ICollectionService collectionService;

    @XxlJob("syncThumbHandler")
    public void syncThumbHandler() {
        log.debug("同步点赞数据到数据库");
        thumbService.syncThumb();
    }

    @XxlJob("syncCollectionHandler")
    public void syncCollectionHandler() {
        log.debug("同步收藏数据到数据库");
        collectionService.syncCollection();
    }

}
