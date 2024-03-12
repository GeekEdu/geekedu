package com.zch.course.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import com.zch.course.enums.LiveVideoStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("live_video")
public class LiveVideo extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 章节id
     */
    private Integer chapterId;

    /**
     * 是否显示
     */
    private Boolean isShow;

    /**
     * 状态
     */
    private LiveVideoStatusEnum status;

    /**
     * 直播时间
     */
    private LocalDateTime liveTime;

    /**
     * 录播id
     */
    private String recordId;

    /**
     * 预估直播时间 秒
     */
    private Long estimateDuration;

    /**
     * 实际直播时间 秒
     */
    private Long duration;

    /**
     * 观看次数
     */
    private Integer viewCount;

    @TableLogic
    private Boolean isDelete;

}
