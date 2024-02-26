package com.zch.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.course.domain.po.CourseSection;
import com.zch.course.mapper.CourseSectionMapper;
import com.zch.course.service.ICourseSectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/2/26
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseSectionServiceImpl extends ServiceImpl<CourseSectionMapper, CourseSection> implements ICourseSectionService {
}
