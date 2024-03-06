package com.zch.oss.domain.vo;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DogeCloudUploadVO extends BaseVO {

    private String s3Endpoint;

    private Map<String, String> credentials = new HashMap<>(0);

    private String did;

    private String key;

    private String s3Bucket;

}
