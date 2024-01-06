package com.zch.user.mapper;

import com.zch.user.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Poison02
 */
@Mapper
public interface UserMapper {

    User selectByUserNameOrPhoneUser(@Param("userName") String userName, @Param("phone") String phone);

    int deleteById(Long id);

    int insert(User user);

    User selectById(Long id);

    int updateById(User user);
}
