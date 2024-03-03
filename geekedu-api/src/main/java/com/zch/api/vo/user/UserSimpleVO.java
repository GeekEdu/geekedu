package com.zch.api.vo.user;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserSimpleVO extends BaseVO {

    private Long userId;

    private String userName;

    private String name;

    private String avatar;

    /**
     * ip 地址
     */
    private String ipAddress;

    /**
     * ip 对应的省份
     */
    private String province;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;



}
