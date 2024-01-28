package com.zch.api.vo.resources;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileUploadVO extends BaseVO {

    private Long id;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 在云端的文件名
     */
    private String keyId;

    /**
     * 文件完整链接
     */
    private String link;

    /**
     * 平台
     */
    private String platform;

    private String createdTime;

}
