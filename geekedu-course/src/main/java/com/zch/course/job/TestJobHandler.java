package com.zch.course.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Poison02
 * @date 2024/3/22
 */
@Component
@Slf4j
public class TestJobHandler {

    @XxlJob("testJobHandler")
    public void testJobHandler() {
        XxlJobHelper.log("testJobHandler.start");
        log.debug("1234556");
        System.out.println("1111111111111111111");
    }

}
