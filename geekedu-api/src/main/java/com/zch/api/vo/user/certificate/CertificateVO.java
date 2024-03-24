package com.zch.api.vo.user.certificate;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CertificateVO extends BaseVO {

    private Integer id;

    /**
     * 证书名
     */
    private String name;

    /**
     * 证书参数
     */
    private String params;

    /**
     * 模版图片
     */
    private String templateImage;

    /**
     * 授予人数
     */
    private Integer userCount;

    private LocalDateTime createdTime;

    private List<Object> relation;

}
