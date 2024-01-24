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
public class Player {

    private String cover;

    private String enabledBulletSecret;

    private BulletSecret bulletSecret;

}
