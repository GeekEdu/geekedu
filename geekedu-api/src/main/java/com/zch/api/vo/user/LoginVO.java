package com.zch.api.vo.user;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录成功返回实体
 * @author Poison02
 * @date 2024/1/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginVO extends BaseVO {

    private String token;

}
