package com.zch.ask.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.ask.domain.po.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/2
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    List<Question> getQuestionPageByCondition(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("sort") String sort, @Param("order") String order,
                                              @Param("userId") Long userId, @Param("categoryId") Integer categoryId, @Param("status") Integer status, @Param("keywords") String keywords);

}
