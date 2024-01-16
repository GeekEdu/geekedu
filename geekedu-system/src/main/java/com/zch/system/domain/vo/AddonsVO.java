package com.zch.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddonsVO implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Boolean enabled;

    private String index_route = "";

    private String index_url = "";

    private String name;

    private List<Object> required = new ArrayList<>();

    private String sign;

    private String version;

}
