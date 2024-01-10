package com.zch.label.domain.dto;

import com.zch.label.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Poison02
 * @date 2024/1/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;

    private String name;

    private String type;

    public static CategoryDTO of(Long id, String name, Short type) {
        return new CategoryDTO(id, name, CategoryEnum.returnDesc(type));
    }

}
