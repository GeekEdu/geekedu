package com.zch.label.domain.dto;

import com.zch.label.enums.CategoryEnum;
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
@ApiModel(description = "分类实体")
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    @ApiModelProperty(value = "分类id", example = "159194974104")
    private Long id;

    @ApiModelProperty(value = "分类名称", example = "后端开发")
    private String name;

    @ApiModelProperty(value = "分类类型", example = "课程")
    private String type;

    public static CategoryDTO of(Long id, String name, Short type) {
        return new CategoryDTO(id, name, CategoryEnum.returnDesc(type));
    }

}
