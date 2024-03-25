package com.zch.api.vo.resources;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AttachVO extends BaseVO {

    private Integer id;

    private String name;

    private String path;

    private String extension;

    private Integer courseId;

    private Long size;

    private LocalDateTime createdTime;

}
