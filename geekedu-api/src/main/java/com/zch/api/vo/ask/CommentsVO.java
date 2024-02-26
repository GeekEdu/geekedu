package com.zch.api.vo.ask;

import com.zch.api.vo.course.CourseSimpleVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/20
 */
@Data
public class CommentsVO extends BaseVO {

    private Integer id;

    private Integer answerId;

    private Integer relationId;

    private Long userId;

    private String content;

    private String cType;

    private LocalDateTime createdTime;

    private UserSimpleVO user;

    private CourseSimpleVO course;

}
