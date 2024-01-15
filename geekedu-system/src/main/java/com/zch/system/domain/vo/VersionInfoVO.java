package com.zch.system.domain.vo;

import com.zch.common.domain.entity.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Poison02
 * @date 2024/1/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionInfoVO extends BaseVO {

    private String springbootVersion;

    private String geekeduVersion;

    private String jdkVersion;

    public static VersionInfoVO of(String springbootVersion, String geekeduVersion, String jdkVersion) {
        return new VersionInfoVO(springbootVersion, geekeduVersion, jdkVersion);
    }

}
