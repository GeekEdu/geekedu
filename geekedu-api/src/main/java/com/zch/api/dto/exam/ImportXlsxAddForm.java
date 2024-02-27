package com.zch.api.dto.exam;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/27
 */
@Data
public class ImportXlsxAddForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private List<List<String>> data;

    private Integer line;

}
