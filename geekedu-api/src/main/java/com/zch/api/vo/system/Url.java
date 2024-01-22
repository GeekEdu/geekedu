package com.zch.api.vo.system;

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
public class Url implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String api;

    private String pc;

    private String wx;

}
