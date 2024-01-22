package com.zch.api.vo.user;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleVO extends BaseVO {

    private Integer id;

    private String description;

    private String disPlayName;

    private String slug;

    private List<Integer> permissionIds;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private PivotVO pivot;

}
