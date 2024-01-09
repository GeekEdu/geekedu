package com.zch.api.feignClient.label;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Poison02
 * @date 2024/1/9
 */
@FeignClient(contextId = "label", value = "label-service", path = "label")
public interface LabelFeignClient {

}
