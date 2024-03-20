package com.zch.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.course.domain.po.LearnCourse;
import com.zch.course.mapper.LearnCourseMapper;
import com.zch.course.service.ILearnCourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/3/20
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LearnCourseServiceImpl extends ServiceImpl<LearnCourseMapper, LearnCourse> implements ILearnCourseService {
}
