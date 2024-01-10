package com.zch.oss.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {

    private Long id;

    private String fileName;

    private String path;

    public static FileDTO of(Long id, String filename, String path){
        return new FileDTO(id, filename, path);
    }
}
