package com.zch.course.service;

import com.zch.course.domain.repository.CourseInfoEs;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/22
 */
public interface EsCourseInfoService {

    boolean insert(CourseInfoEs subjectInfoEs);

    List<CourseInfoEs> queryCourseInfoList(CourseInfoEs courseInfoEs);

}
