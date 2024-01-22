package com.zch.api.dto.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/1/22
 */
@Data
public class GraphForm {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonIgnore
    private LocalDateTime startAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonIgnore
    private LocalDateTime endAt;

}
