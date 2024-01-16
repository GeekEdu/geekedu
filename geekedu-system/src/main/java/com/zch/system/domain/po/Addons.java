package com.zch.system.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Addons implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Boolean enabled;

    private String indexRoute;

    private String indexUrl;

    private String name;

    private List<Object> required;

    private String sign;

    private String version;

}
