package com.zch.user.mapper;

import com.zch.user.domain.po.UserDetail;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Poison02
 */
@Mapper
public interface UserDetailMapper {

    int deleteById(Long id);

    int insert(UserDetail userDetail);

    UserDetail selectById(Long id);

    int updateById(UserDetail userDetail);
}
