package com.zch.api.vo.user;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信小程序登录VO
 * @author Poison02
 * @date 2024/3/31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxLoginVO extends BaseVO {

    private Long id;

    private String userName;

    private String name;

    private String phone;

    private String avatar;

    private String sex;

    private String email;

    private String token;

}
