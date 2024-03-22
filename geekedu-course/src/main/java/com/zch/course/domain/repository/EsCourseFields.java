package com.zch.course.domain.repository;

/**
 * @author Poison02
 * @date 2024/3/22
 */
public class EsCourseFields {

    public static final String COURSE_ID = "course_id";

    public static final String DOC_ID = "doc_id";

    /**
     * 课程标题
     */
    public static final String TITLE = "title";

    /**
     * 课程价格
     */
    public static final String PRICE = "price";

    /**
     * 课程描述
     */
    public static final String DESCRIPTION = "description";

    /**
     * 详细介绍
     */
    public static final String INTRO = "intro";

    /**
     * 课程封面
     */
    public static final String COVER_LINK = "coverLink";

    /**
     * 上架时间
     */
    public static final String GROUNDING_TIME = "grounding_time";

    /**
     * 分类id
     */
    public static final String CATEGORY_ID = "categoryId";

    public static final String CREATED_TIME = "created_time";

    public static final String UPDATED_TIME = "updated_time";

    public static final String[] FIELD_QUERY = {
            TITLE, DESCRIPTION, INTRO
    };

}
