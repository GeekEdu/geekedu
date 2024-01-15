package com.zch.system.controller;

import com.zch.common.domain.result.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Poison02
 * @date 2024/1/14
 */
@RestController
@RequestMapping("/api/v2/other")
public class ConfigController {

    @GetMapping("/config")
    public Response getConfig() {
        return Response.success();
    }

}
