package com.zch.label.domain.dto;

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
public class TagDTO {

    private Long id;

    private String name;

    public static TagDTO of(Long id, String name) {
        return new TagDTO(id, name);
    }

}
