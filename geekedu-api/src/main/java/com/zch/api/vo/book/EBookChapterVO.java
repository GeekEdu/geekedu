package com.zch.api.vo.book;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EBookChapterVO extends BaseVO {

    private Integer id;

    /**
     * 章节名
     */
    private String name;

    /**
     * 电子书 id
     */
    private Integer bookId;

    /**
     * 排序
     */
    private Integer sort;

    private LocalDateTime createdTime;

}
