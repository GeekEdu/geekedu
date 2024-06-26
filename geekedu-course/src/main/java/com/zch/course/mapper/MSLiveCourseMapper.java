package com.zch.course.mapper;

import com.zch.common.meilisearch.reposotory.impl.MeiliSearchRepository;
import com.zch.course.domain.dto.LiveCourseMSDTO;
import org.springframework.stereotype.Repository;

/**
 * @author Poison02
 * @date 2024/4/2
 */
@Repository
public class MSLiveCourseMapper extends MeiliSearchRepository<LiveCourseMSDTO> {
}
