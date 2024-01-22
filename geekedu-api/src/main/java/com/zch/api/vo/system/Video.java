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
public class Video implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String default_service;

}
