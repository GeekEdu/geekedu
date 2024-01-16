package com.zch.system.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/1/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class System implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String logo;

    private String version;

    private Url url;

}
