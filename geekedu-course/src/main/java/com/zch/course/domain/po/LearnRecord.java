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

    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 课程小节id
     */
    private Integer videoId;

    /**
     * 课程类型 course live
     */
    private String type;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 观看时长
     */
    private Integer duration;

    /**
     * 总时长 直播课就为0
     */
    private Integer total;

    @TableLogic
    private Boolean isDelete;

}
