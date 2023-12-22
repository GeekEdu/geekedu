package com.zch.oss.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Poison02
 * @date 2023/12/22
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {

    private String fileName;

    private Boolean directoryFlag;

    private String etag;

}
