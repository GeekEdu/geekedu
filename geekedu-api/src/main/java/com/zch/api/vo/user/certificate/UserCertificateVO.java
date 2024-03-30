package com.zch.api.vo.user.certificate;

import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserCertificateVO extends BaseVO {

    private Integer id;

    private Integer cId;

    private String cNum;

    private Long userId;

    private LocalDateTime createdTime;

    private UserSimpleVO user;

}
