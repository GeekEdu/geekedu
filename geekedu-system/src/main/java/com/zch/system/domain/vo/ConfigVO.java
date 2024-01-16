package com.zch.system.domain.vo;

import com.zch.system.domain.po.System;
import com.zch.system.domain.po.Video;
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
public class ConfigVO implements Serializable {

    private static final Long serialVersionUID = 1L;

    private System system;

    private Video video;

}
