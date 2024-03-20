package com.zch.course.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("learn_record")
public class LearnRecord extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer courseId;

    private Integer videoId;

    private String type;

    private Long userId;

    private Integer duration;

    @TableLogic
    private Boolean isDelete;

}
