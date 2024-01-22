package com.zch.api.vo.user;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/1/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRoleVO extends BaseVO {

    private Long id;

    private String email;

    private Short isBanLogin;

    private Boolean isSuper;

    private Integer loginCount;

    private String name;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private LocalDateTime LastLoginDate;

    private String lastLoginIp;

    private List<Integer> roleId;

    private List<RoleVO> roles;

    private Map<String, Integer> permissions;

}
