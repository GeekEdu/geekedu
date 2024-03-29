package com.zch.api.vo.exam;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TagsVO extends BaseVO {

    private Integer id;

    private String name;

    private String sort;

    private Integer parentId;

    private List<CTagsVO> children = new ArrayList<>(0);

    private LocalDateTime createdTime;

}
