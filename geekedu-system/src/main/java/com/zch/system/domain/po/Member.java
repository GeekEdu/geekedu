package com.zch.system.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Poison02
 * @date 2024/1/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private Integer enabledMobileBindAlert;

    private Boolean enabledFaceVerify;

}
