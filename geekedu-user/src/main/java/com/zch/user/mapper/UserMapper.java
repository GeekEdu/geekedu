package com.zch.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.user.domain.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Poison02
 * @date 2024/1/11
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
