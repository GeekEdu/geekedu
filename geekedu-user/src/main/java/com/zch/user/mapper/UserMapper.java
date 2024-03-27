package com.zch.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.user.domain.dto.UserCountDTO;
import com.zch.user.domain.po.User;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/11
 */
public interface UserMapper extends BaseMapper<User> {

    List<UserCountDTO> queryRegisterCount();

}
