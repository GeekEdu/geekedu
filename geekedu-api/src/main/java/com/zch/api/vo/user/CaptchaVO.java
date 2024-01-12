package com.zch.api.vo.user;

import com.zch.common.domain.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CaptchaVO extends BaseVO {

    /**
     * 验证码生成的 base64 编码
     */
    private String img;

    /**
     * 每个验证码生成的随机key
     */
    private String key;

    private boolean sensitive = false;

}
