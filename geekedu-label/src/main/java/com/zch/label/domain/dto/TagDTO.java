package com.zch.label.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Poison02
 * @date 2024/1/7
 */
@Data
@ApiModel(description = "标签实体")
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    @ApiModelProperty(value = "标签id", example = "1243535325", required = false)
    private Long id;

    @ApiModelProperty(value = "标签名称", example = "SpringBoot", required = false)
    private String name;

    public static TagDTO of(Long id, String name) {
        return new TagDTO(id, name);
    }

}
