package com.zch.ask.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.ask.domain.po.Answer;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Poison02
 * @date 2024/1/27
 */
@Mapper
public interface AskMapper extends BaseMapper<Answer> {
}
